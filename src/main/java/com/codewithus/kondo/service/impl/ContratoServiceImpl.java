package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Contrato;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.dto.contrato.ContratoRequestDTO;
import com.codewithus.kondo.dto.contrato.ContratoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.ContratoMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.repository.specification.ContratoSpecifications;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CondominioRepository condominioRepository;
    private final ContratoMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public ContratoResponseDTO salvar(ContratoRequestDTO dto) {
        validarDatas(dto);

        Fornecedor fornecedor = buscarFornecedor(dto.fornecedorId());
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        validarSobreposicao(dto, null);
        Contrato entity = mapper.toEntity(dto, fornecedor, condominio);
        return mapper.toResponseDTO(contratoRepository.save(entity));
    }

    @Override
    public ContratoResponseDTO buscarPorId(UUID id) {
        Contrato contrato = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(contrato.getCondominio().getId());
        return mapper.toResponseDTO(contrato);
    }

    @Override
    public Page<ContratoResponseDTO> listar(UUID condominioId, UUID fornecedorId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (condominioId != null) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new BusinessException("Data inicial não pode ser maior que a data final");
        }

        Specification<Contrato> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            specification = specification.and(ContratoSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(ContratoSpecifications.hasCondominioId(condominioId));
        }

        if (fornecedorId != null) {
            specification = specification.and(ContratoSpecifications.hasFornecedorId(fornecedorId));
        }

        if (dataInicio != null) {
            specification = specification.and(ContratoSpecifications.vigenciaTerminaAposOuSemFim(dataInicio));
        }

        if (dataFim != null) {
            specification = specification.and(ContratoSpecifications.vigenciaComecaAntesOuNoFim(dataFim));
        }

        Page<Contrato> contratos = contratoRepository.findAll(specification, pageable);

        return contratos.map(mapper::toResponseDTO);
    }

    @Override
    public ContratoResponseDTO atualizar(UUID id, ContratoRequestDTO dto) {
        validarDatas(dto);

        Contrato entity = buscarEntidade(id);
        Fornecedor fornecedor = buscarFornecedor(dto.fornecedorId());
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(entity.getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        validarSobreposicao(dto, id);
        mapper.updateEntity(entity, dto, fornecedor, condominio);
        return mapper.toResponseDTO(contratoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        Contrato contrato = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(contrato.getCondominio().getId());
        contratoRepository.delete(contrato);
    }

    private Contrato buscarEntidade(UUID id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado"));
    }

    private Fornecedor buscarFornecedor(UUID id) {
        return fornecedorRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private void validarDatas(ContratoRequestDTO dto){
        if (dto.dataFim() != null && dto.dataFim().isBefore(dto.dataInicio())){
            throw new BusinessException("Data fim não pode ser anterior à data de início");
        }
    }

    private void validarSobreposicao(ContratoRequestDTO dto, UUID contratoAtualId) {
        boolean possuiSobreposicao = contratoRepository.existsContratoSobreposto(
                dto.fornecedorId(),
                dto.condominioId(),
                dto.dataInicio(),
                dto.dataFim(),
                contratoAtualId
        );

        if (possuiSobreposicao) {
            throw new BusinessException("Já existe contrato com vigência sobreposta para este fornecedor no condomínio informado");
        }
    }
}
