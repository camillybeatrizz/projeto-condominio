package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Pagamento;
import com.codewithus.kondo.dto.pagamento.PagamentoRequestDTO;
import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.PagamentoMapper;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.repository.specification.PagamentoSpecifications;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.security.CurrentUserResolver;
import com.codewithus.kondo.service.AuditoriaService;
import com.codewithus.kondo.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoServiceImpl implements PagamentoService {

    private static final String TIPO_AUDITORIA_COBRANCA_PAGA = "COBRANCA_MARCADA_PAGA";

    private final PagamentoRepository pagamentoRepository;
    private final CobrancaRepository cobrancaRepository;
    private final PagamentoMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CurrentUserResolver currentUserResolver;
    private final CondominioScopeService condominioScopeService;
    private final AuditoriaService auditoriaService;

    @Override
    public PagamentoResponseDTO salvar(PagamentoRequestDTO dto) {
        Cobranca cobranca = buscarCobranca(dto.cobrancaId());
        validarEscopoCondominio(cobranca);

        if (cobranca.getStatus() == StatusCobrancaEnum.PAGA){
            throw new BusinessException("Não é permitido registrar pagamento para uma cobrança já paga");
        }

        pagamentoRepository.findByTransactionId(dto.transactionId())
                .ifPresent(existing ->{
                    throw new ConflictException("Já existe pagamento com este transactionId");
                });

        Pagamento entity = mapper.toEntity(dto, cobranca);
        Pagamento saved = pagamentoRepository.save(entity);

        cobranca.setStatus(StatusCobrancaEnum.PAGA);
        cobrancaRepository.save(cobranca);

        auditoriaService.registrar(
                TIPO_AUDITORIA_COBRANCA_PAGA,
                "COBRANCA",
                cobranca.getId(),
                resolveAuditActor(),
                "Cobranca baixada para PAGA a partir de pagamento manual " + saved.getTransactionId()
        );

        return mapper.toResponseDTO(saved);
    }

    @Override
    public PagamentoResponseDTO buscarPorId(UUID id) {
        Pagamento pagamento = buscarEntidade(id);
        if (!authenticatedUserFacade.isMorador()) {
            validarEscopoCondominio(pagamento.getCobranca());
        }
        return mapper.toResponseDTO(pagamento);
    }

    @Override
    public Page<PagamentoResponseDTO> listar(UUID condominioId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (condominioId != null && authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new BusinessException("Data inicial não pode ser maior que a data final");
        }

        Specification<Pagamento> specification = PagamentoSpecifications.isNotDeleted();

        if (authenticatedUserFacade.isMorador()) {
            if (currentUserResolver.findCurrentUsuarioId().isEmpty()) {
                return Page.empty(pageable);
            }
            specification = specification.and(PagamentoSpecifications.hasMoradorId(currentUserResolver.getRequiredUsuarioId()));
        }

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin() && !authenticatedUserFacade.isMorador()) {
            specification = specification.and(PagamentoSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(PagamentoSpecifications.hasCondominioId(condominioId));
        }

        if (dataInicio != null) {
            specification = specification.and(PagamentoSpecifications.dataPagamentoMaiorOuIgual(dataInicio));
        }

        if (dataFim != null) {
            specification = specification.and(PagamentoSpecifications.dataPagamentoMenorOuIgual(dataFim));
        }

        Page<Pagamento> pagamentos = pagamentoRepository.findAll(specification, pageable);

        return pagamentos.map(mapper::toResponseDTO);
    }

    @Override
    public PagamentoResponseDTO atualizar(UUID id, PagamentoRequestDTO dto) {
        Pagamento entity = buscarEntidade(id);
        Cobranca cobranca = buscarCobranca(dto.cobrancaId());
        validarEscopoCondominio(entity.getCobranca());
        validarEscopoCondominio(cobranca);

        pagamentoRepository.findByTransactionId(dto.transactionId())
                        .ifPresent(existing ->{
                            if (!existing.getId().equals(id)) {
                                throw new ConflictException("Já existe pagamento com este transactionId");
                            }
                        });

        mapper.updateEntity(entity, dto, cobranca);
        return mapper.toResponseDTO(pagamentoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        Pagamento pagamento = buscarEntidade(id);
        validarEscopoCondominio(pagamento.getCobranca());
        pagamento.setDeletedAt(java.time.LocalDateTime.now());
        pagamento.setDeletedBy(resolveAuditActor());
        pagamentoRepository.save(pagamento);
    }

    private Pagamento buscarEntidade(UUID id) {
        return pagamentoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));
    }

    private Cobranca buscarCobranca(UUID id) {
        return cobrancaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança não encontrada"));
    }

    private void validarEscopoCondominio(Cobranca cobranca) {
        if (cobranca == null || cobranca.getUnidade() == null || cobranca.getUnidade().getBloco() == null
                || cobranca.getUnidade().getBloco().getCondominio() == null) {
            return;
        }

        condominioScopeService.assertCanAccessCondominio(cobranca.getUnidade().getBloco().getCondominio().getId());
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }
}
