package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Despesa;
import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.DespesaMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.DespesaRepository;
import com.codewithus.kondo.repository.specification.DespesaSpecifications;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.DespesaService;
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
public class DespesaServiceImpl implements DespesaService {

    private final DespesaRepository despesaRepository;
    private final CondominioRepository condominioRepository;
    private final DespesaMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public DespesaResponseDTO salvar(DespesaRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        Despesa entity = mapper.toEntity(dto, condominio);
        return mapper.toResponseDTO(despesaRepository.save(entity));
    }

    @Override
    public DespesaResponseDTO buscarPorId(UUID id) {
        Despesa despesa = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(despesa.getCondominio().getId());
        return mapper.toResponseDTO(despesa);
    }

    @Override
    public Page<DespesaResponseDTO> listar(
            UUID condominioId,
            CategoriaDespesaEnum categoria,
            LocalDate dataInicio,
            LocalDate dataFim,
            Pageable pageable
    ) {
        if (condominioId != null) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new BusinessException("Data inicial não pode ser maior que a data final");
        }

        Specification<Despesa> specification = DespesaSpecifications.isNotDeleted();

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            specification = specification.and(DespesaSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(DespesaSpecifications.hasCondominioId(condominioId));
        }

        if (categoria != null) {
            specification = specification.and(DespesaSpecifications.hasCategoria(categoria));
        }

        if (dataInicio != null) {
            specification = specification.and(DespesaSpecifications.hasDataMaiorOuIgual(dataInicio));
        }

        if (dataFim != null) {
            specification = specification.and(DespesaSpecifications.hasDataMenorOuIgual(dataFim));
        }

        Page<Despesa> despesas = despesaRepository.findAll(specification, pageable);

        return despesas.map(mapper::toResponseDTO);
    }

    @Override
    public DespesaResponseDTO atualizar(UUID id, DespesaRequestDTO dto) {
        Despesa entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(entity.getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        mapper.updateEntity(entity, dto, condominio);
        return mapper.toResponseDTO(despesaRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        Despesa despesa = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(despesa.getCondominio().getId());
        despesa.setDeletedAt(java.time.LocalDateTime.now());
        despesa.setDeletedBy(resolveAuditActor());
        despesaRepository.save(despesa);
    }

    private Despesa buscarEntidade(UUID id) {
        return despesaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }
}
