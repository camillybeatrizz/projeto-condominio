package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.BlocoMapper;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.BlocoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlocoServiceImpl implements BlocoService {

    private final BlocoRepository blocoRepository;
    private final CondominioRepository condominioRepository;
    private final UnidadeRepository unidadeRepository;
    private final BlocoMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public BlocoResponseDTO salvar(BlocoRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        validarNomeDuplicado(dto.nome(), condominio.getId(), null);
        Bloco entity = mapper.toEntity(dto, condominio);
        return mapper.toResponseDTO(blocoRepository.save(entity));
    }

    @Override
    public BlocoResponseDTO buscarPorId(UUID id) {
        Bloco bloco = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(bloco.getCondominio().getId());
        return mapper.toResponseDTO(bloco);
    }

    @Override
    public Page<BlocoResponseDTO> listar(UUID condominioId, Pageable pageable) {
        if (condominioId != null) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Page<Bloco> blocos = authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()
                ? condominioId != null
                ? blocoRepository.findAllByCondominio_IdAndDeletedAtIsNull(condominioId, pageable)
                : blocoRepository.findAllByCondominio_IdInAndDeletedAtIsNull(condominioScopeService.getCondominioIdsPermitidos(), pageable)
                : condominioId != null
                ? blocoRepository.findAllByCondominio_IdAndDeletedAtIsNull(condominioId, pageable)
                : blocoRepository.findAllByDeletedAtIsNull(pageable);

        return blocos.map(mapper::toResponseDTO);
    }

    @Override
    public BlocoResponseDTO atualizar(UUID id, BlocoRequestDTO dto) {
        Bloco entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());
        condominioScopeService.assertCanAccessCondominio(entity.getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(condominio.getId());
        validarNomeDuplicado(dto.nome(), condominio.getId(), id);
        mapper.updateEntity(entity, dto, condominio);
        return mapper.toResponseDTO(blocoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        Bloco bloco = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(bloco.getCondominio().getId());
        if (unidadeRepository.existsByBloco_IdAndDeletedAtIsNull(id)) {
            throw new BusinessException("Não é permitido excluir bloco que ainda possui unidades vinculadas");
        }
        bloco.setDeletedAt(java.time.LocalDateTime.now());
        bloco.setDeletedBy(resolveAuditActor());
        blocoRepository.save(bloco);
    }

    private Bloco buscarEntidade(UUID id) {
        return blocoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloco não encontrado"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private void validarNomeDuplicado(String nome, UUID condominioId, UUID blocoAtualId) {
        blocoRepository.findByCondominio_IdAndNomeIgnoreCaseAndDeletedAtIsNull(condominioId, nome)
                .ifPresent(bloco -> {
                    if (blocoAtualId == null || !bloco.getId().equals(blocoAtualId)) {
                        throw new ConflictException("Já existe bloco com este nome no condomínio informado");
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
