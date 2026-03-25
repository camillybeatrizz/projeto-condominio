package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Chamado;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.ChamadoMapper;
import com.codewithus.kondo.repository.ChamadoRepository;
import com.codewithus.kondo.repository.specification.ChamadoSpecifications;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.security.CurrentUserResolver;
import com.codewithus.kondo.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChamadoServiceImpl implements ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ChamadoMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CurrentUserResolver currentUserResolver;
    private final CondominioScopeService condominioScopeService;

    @Override
    public ChamadoResponseDTO salvar(ChamadoRequestDTO dto) {
        validarCriacao(dto);
        Unidade unidade = buscarUnidade(dto.unidadeId());
        if (!authenticatedUserFacade.isMorador()) {
            condominioScopeService.assertCanAccessCondominio(unidade.getBloco().getCondominio().getId());
        }
        Chamado entity = mapper.toEntity(dto, unidade);
        return mapper.toResponseDTO(chamadoRepository.save(entity));
    }

    @Override
    public ChamadoResponseDTO buscarPorId(UUID id) {
        Chamado chamado = buscarEntidade(id);
        if (!authenticatedUserFacade.isMorador()) {
            condominioScopeService.assertCanAccessCondominio(chamado.getUnidade().getBloco().getCondominio().getId());
        }
        return mapper.toResponseDTO(chamado);
    }

    @Override
    public Page<ChamadoResponseDTO> listar(UUID condominioId, StatusChamadoEnum status, Pageable pageable) {
        if (condominioId != null && authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Specification<Chamado> specification = ChamadoSpecifications.isNotDeleted();

        if (authenticatedUserFacade.isMorador()) {
            if (currentUserResolver.findCurrentUsuarioId().isEmpty()) {
                return Page.empty(pageable);
            }
            specification = specification.and(ChamadoSpecifications.hasMoradorId(currentUserResolver.getRequiredUsuarioId()));
        }

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin() && !authenticatedUserFacade.isMorador()) {
            specification = specification.and(ChamadoSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(ChamadoSpecifications.hasCondominioId(condominioId));
        }

        if (status != null) {
            specification = specification.and(ChamadoSpecifications.hasStatus(status));
        }

        Page<Chamado> chamados = chamadoRepository.findAll(specification, pageable);

        return chamados.map(mapper::toResponseDTO);
    }

    @Override
    public ChamadoResponseDTO atualizar(UUID id, ChamadoRequestDTO dto) {
        Chamado entity = buscarEntidade(id);
        validarAtualizacao(entity, dto);
        Unidade unidade = buscarUnidade(dto.unidadeId());
        condominioScopeService.assertCanAccessCondominio(entity.getUnidade().getBloco().getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(unidade.getBloco().getCondominio().getId());
        mapper.updateEntity(entity, dto, unidade);
        return mapper.toResponseDTO(chamadoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        Chamado chamado = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(chamado.getUnidade().getBloco().getCondominio().getId());
        chamado.setDeletedAt(java.time.LocalDateTime.now());
        chamado.setDeletedBy(resolveAuditActor());
        chamadoRepository.save(chamado);
    }

    private Chamado buscarEntidade(UUID id) {
        return chamadoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));
    }

    private Unidade buscarUnidade(UUID id) {
        return unidadeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    private void validarCriacao(ChamadoRequestDTO dto) {
        validarDataAbertura(dto.dataAbertura());
        if (dto.status() != StatusChamadoEnum.ABERTO) {
            throw new BusinessException("Chamado deve ser criado com status ABERTO");
        }
    }

    private void validarAtualizacao(Chamado entity, ChamadoRequestDTO dto) {
        validarDataAbertura(dto.dataAbertura());

        if (!transicaoPermitida(entity.getStatus(), dto.status())) {
            throw new BusinessException("Transição de status do chamado não é permitida");
        }
    }

    private void validarDataAbertura(LocalDate dataAbertura) {
        if (dataAbertura.isAfter(LocalDate.now())) {
            throw new BusinessException("Data de abertura do chamado não pode ser futura");
        }
    }

    private boolean transicaoPermitida(StatusChamadoEnum statusAtual, StatusChamadoEnum novoStatus) {
        if (statusAtual == novoStatus) {
            return true;
        }

        return switch (statusAtual) {
            case ABERTO -> novoStatus == StatusChamadoEnum.ANDAMENTO || novoStatus == StatusChamadoEnum.CONCLUIDO;
            case ANDAMENTO -> novoStatus == StatusChamadoEnum.CONCLUIDO;
            case CONCLUIDO -> false;
        };
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }
}
