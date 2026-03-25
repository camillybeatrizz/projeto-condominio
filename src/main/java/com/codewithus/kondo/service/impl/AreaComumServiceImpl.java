package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.AreaComum;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.dto.areacomum.AreaComumRequestDTO;
import com.codewithus.kondo.dto.areacomum.AreaComumResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.AreaComumMapper;
import com.codewithus.kondo.repository.AreaComumRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.AreaComumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AreaComumServiceImpl implements AreaComumService {

    private final AreaComumRepository areaComumRepository;
    private final CondominioRepository condominioRepository;
    private final AreaComumMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public AreaComumResponseDTO salvar(AreaComumRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        validarEscopoCondominio(condominio.getId());
        validarDuplicidade(dto.nome(), condominio.getId(), null);

        AreaComum entity = mapper.toEntity(dto, condominio);
        entity = areaComumRepository.save(entity);
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public AreaComumResponseDTO buscarPorId(UUID id) {
        AreaComum areaComum = buscarEntidade(id);
        validarEscopoCondominio(areaComum.getCondominio().getId());
        return mapper.toResponseDTO(areaComum);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AreaComumResponseDTO> listar(UUID condominioId, Pageable pageable) {
        if (condominioId != null) {
            validarEscopoCondominio(condominioId);
            return areaComumRepository.findAllByCondominio_IdAndDeletedAtIsNull(condominioId, pageable)
                    .map(mapper::toResponseDTO);
        }

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            return areaComumRepository.findAllByCondominio_IdInAndDeletedAtIsNull(
                            condominioScopeService.getCondominioIdsPermitidos(),
                            pageable
                    )
                    .map(mapper::toResponseDTO);
        }

        return areaComumRepository.findAllByDeletedAtIsNull(pageable)
                .map(mapper::toResponseDTO);
    }

    @Override
    public AreaComumResponseDTO atualizar(UUID id, AreaComumRequestDTO dto) {
        AreaComum entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());

        validarEscopoCondominio(entity.getCondominio().getId());
        validarEscopoCondominio(condominio.getId());
        validarDuplicidade(dto.nome(), condominio.getId(), id);

        mapper.updateEntity(entity, dto, condominio);
        entity = areaComumRepository.save(entity);
        return mapper.toResponseDTO(entity);
    }

    @Override
    public void deletar(UUID id) {
        AreaComum entity = buscarEntidade(id);
        validarEscopoCondominio(entity.getCondominio().getId());
        entity.setDeletedAt(java.time.LocalDateTime.now());
        entity.setDeletedBy(resolveAuditActor());
        areaComumRepository.save(entity);
    }

    private AreaComum buscarEntidade(UUID id) {
        return areaComumRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area comum não encontrada"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private void validarDuplicidade(String nome, UUID condominioId, UUID areaComumIdAtual) {
        areaComumRepository.findByCondominio_IdAndNomeIgnoreCaseAndDeletedAtIsNull(condominioId, nome)
                .ifPresent(existing -> {
                    if (areaComumIdAtual == null || !existing.getId().equals(areaComumIdAtual)) {
                        throw new ConflictException("Já existe área comum com este nome no condomínio informado");
                    }
                });
    }

    private void validarEscopoCondominio(UUID condominioId) {
        if (condominioId == null) {
            throw new BusinessException("Condominio da área comum é obrigatório");
        }

        condominioScopeService.assertCanAccessCondominio(condominioId);
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }
}
