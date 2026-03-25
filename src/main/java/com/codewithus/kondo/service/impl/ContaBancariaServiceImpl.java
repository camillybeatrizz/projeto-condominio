package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.ContaBancaria;
import com.codewithus.kondo.domain.enums.TipoContaEnum;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaRequestDTO;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaResponseDTO;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.ContaBancariaMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.ContaBancariaRepository;
import com.codewithus.kondo.repository.specification.ContaBancariaSpecifications;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.ContaBancariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaBancariaServiceImpl implements ContaBancariaService {

    private final ContaBancariaRepository contaBancariaRepository;
    private final CondominioRepository condominioRepository;
    private final ContaBancariaMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public ContaBancariaResponseDTO salvar(ContaBancariaRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        validarContaDuplicada(dto, null);
        ContaBancaria entity = mapper.toEntity(dto, condominio);
        return mapper.toResponseDTO(contaBancariaRepository.save(entity));
    }

    @Override
    public ContaBancariaResponseDTO buscarPorId(UUID id) {
        ContaBancaria contaBancaria = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(contaBancaria.getCondominio().getId());
        return mapper.toResponseDTO(contaBancaria);
    }

    @Override
    public Page<ContaBancariaResponseDTO> listar(UUID condominioId, TipoContaEnum tipo, String banco, Pageable pageable) {
        if (condominioId != null) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Specification<ContaBancaria> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            specification = specification.and(ContaBancariaSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(ContaBancariaSpecifications.hasCondominioId(condominioId));
        }

        if (tipo != null) {
            specification = specification.and(ContaBancariaSpecifications.hasTipo(tipo));
        }

        if (banco != null && !banco.isBlank()) {
            specification = specification.and(ContaBancariaSpecifications.hasBancoContaining(banco.trim()));
        }

        Page<ContaBancaria> contas = contaBancariaRepository.findAll(specification, pageable);

        return contas.map(mapper::toResponseDTO);
    }

    @Override
    public ContaBancariaResponseDTO atualizar(UUID id, ContaBancariaRequestDTO dto) {
        ContaBancaria entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(entity.getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        validarContaDuplicada(dto, id);
        mapper.updateEntity(entity, dto, condominio);
        return mapper.toResponseDTO(contaBancariaRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        ContaBancaria contaBancaria = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(contaBancaria.getCondominio().getId());
        contaBancariaRepository.delete(contaBancaria);
    }

    private ContaBancaria buscarEntidade(UUID id) {
        return contaBancariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária não encontrada"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private void validarContaDuplicada(ContaBancariaRequestDTO dto, UUID contaAtualId) {
        contaBancariaRepository.findByCondominio_IdAndAgenciaAndConta(dto.condominioId(), dto.agencia(), dto.conta())
                .ifPresent(contaBancaria -> {
                    if (contaAtualId == null || !contaBancaria.getId().equals(contaAtualId)) {
                        throw new ConflictException("Já existe conta bancária com esta agência e conta no condomínio informado");
                    }
                });
    }
}
