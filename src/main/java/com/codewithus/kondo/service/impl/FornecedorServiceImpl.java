package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.FornecedorMapper;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.repository.specification.FornecedorSpecifications;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ContratoRepository contratoRepository;
    private final FornecedorMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public FornecedorResponseDTO salvar(FornecedorRequestDTO dto) {
        validarCnpjDuplicado(dto.cnpj(), null);
        Fornecedor entity = mapper.toEntity(dto);
        return mapper.toResponseDTO(fornecedorRepository.save(entity));
    }

    @Override
    public FornecedorResponseDTO buscarPorId(UUID id) {
        Fornecedor fornecedor = buscarEntidade(id);
        validarEscopoFornecedor(fornecedor.getId());
        return mapper.toResponseDTO(fornecedor);
    }

    @Override
    public Page<FornecedorResponseDTO> listar(UUID condominioId, String nome, String cnpj, Pageable pageable) {
        if (condominioId != null) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Specification<Fornecedor> specification = FornecedorSpecifications.isNotDeleted()
                .and(FornecedorSpecifications.hasContrato());

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            specification = specification.and(FornecedorSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(FornecedorSpecifications.hasCondominioId(condominioId));
        }

        if (nome != null && !nome.isBlank()) {
            specification = specification.and(FornecedorSpecifications.hasNomeContaining(nome.trim()));
        }

        if (cnpj != null && !cnpj.isBlank()) {
            specification = specification.and(FornecedorSpecifications.hasCnpjContaining(cnpj.trim()));
        }

        Page<Fornecedor> fornecedores = fornecedorRepository.findAll(specification, pageable);

        return fornecedores.map(mapper::toResponseDTO);
    }

    @Override
    public FornecedorResponseDTO atualizar(UUID id, FornecedorRequestDTO dto) {
        Fornecedor entity = buscarEntidade(id);
        validarEscopoFornecedor(id);
        validarCnpjDuplicado(dto.cnpj(), id);
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(fornecedorRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        validarEscopoFornecedor(id);
        if (contratoRepository.existsByFornecedor_Id(id)) {
            throw new BusinessException("Não é permitido excluir fornecedor que possui contratos vinculados");
        }
        Fornecedor fornecedor = buscarEntidade(id);
        fornecedor.setDeletedAt(java.time.LocalDateTime.now());
        fornecedor.setDeletedBy(resolveAuditActor());
        fornecedorRepository.save(fornecedor);
    }

    private Fornecedor buscarEntidade(UUID id) {
        return fornecedorRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
    }

    private void validarEscopoFornecedor(UUID fornecedorId) {
        if (!authenticatedUserFacade.isAuthenticated() || authenticatedUserFacade.isAdmin()) {
            return;
        }

        if (!fornecedorRepository.existsByIdAndDeletedAtIsNullAndContratoCondominioIdIn(fornecedorId, condominioScopeService.getCondominioIdsPermitidos())) {
            throw new org.springframework.security.access.AccessDeniedException("Acesso negado");
        }
    }

    private void validarCnpjDuplicado(String cnpj, UUID fornecedorAtualId) {
        fornecedorRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .ifPresent(fornecedor -> {
                    if (fornecedorAtualId == null || !fornecedor.getId().equals(fornecedorAtualId)) {
                        throw new ConflictException("Já existe fornecedor com este CNPJ");
                    }
                });
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }
}
