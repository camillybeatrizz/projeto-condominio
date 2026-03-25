package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.UnidadeMapper;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.ChamadoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.repository.specification.UnidadeSpecifications;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.security.CurrentUserResolver;
import com.codewithus.kondo.service.UnidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnidadeServiceImpl implements UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final BlocoRepository blocoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CobrancaRepository cobrancaRepository;
    private final ChamadoRepository chamadoRepository;
    private final UnidadeMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CurrentUserResolver currentUserResolver;
    private final CondominioScopeService condominioScopeService;

    @Override
    public UnidadeResponseDTO salvar(UnidadeRequestDTO dto) {
        Bloco bloco = buscarBloco(dto.blocoId());
        condominioScopeService.assertCanAccessCondominio(bloco.getCondominio().getId());
        validarNumeroDuplicado(dto.numero(), bloco.getId(), null);
        Usuario morador = buscarMorador(dto.moradorId());
        Unidade entity = mapper.toEntity(dto, bloco, morador);
        return mapper.toResponseDTO(unidadeRepository.save(entity));
    }

    @Override
    public UnidadeResponseDTO buscarPorId(UUID id) {
        Unidade unidade = buscarEntidade(id);
        if (!authenticatedUserFacade.isMorador()) {
            condominioScopeService.assertCanAccessCondominio(unidade.getBloco().getCondominio().getId());
        }
        return mapper.toResponseDTO(unidade);
    }

    @Override
    public Page<UnidadeResponseDTO> listar(UUID condominioId, UUID blocoId, String tipo, String numero, Pageable pageable) {
        if (condominioId != null && authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Specification<Unidade> specification = UnidadeSpecifications.isNotDeleted();

        if (authenticatedUserFacade.isMorador()) {
            if (currentUserResolver.findCurrentUsuarioId().isEmpty()) {
                return Page.empty(pageable);
            }
            specification = specification.and(UnidadeSpecifications.hasMoradorId(currentUserResolver.getRequiredUsuarioId()));
        }

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin() && !authenticatedUserFacade.isMorador()) {
            specification = specification.and(UnidadeSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(UnidadeSpecifications.hasCondominioId(condominioId));
        }

        if (blocoId != null) {
            specification = specification.and(UnidadeSpecifications.hasBlocoId(blocoId));
        }

        if (tipo != null && !tipo.isBlank()) {
            specification = specification.and(UnidadeSpecifications.hasTipo(tipo.trim()));
        }

        if (numero != null && !numero.isBlank()) {
            specification = specification.and(UnidadeSpecifications.hasNumeroContaining(numero.trim()));
        }

        Page<Unidade> unidades = unidadeRepository.findAll(specification, pageable);

        return unidades.map(mapper::toResponseDTO);
    }

    @Override
    public UnidadeResponseDTO atualizar(UUID id, UnidadeRequestDTO dto) {
        Unidade entity = buscarEntidade(id);
        Bloco bloco = buscarBloco(dto.blocoId());
        condominioScopeService.assertCanAccessCondominio(entity.getBloco().getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(bloco.getCondominio().getId());
        validarNumeroDuplicado(dto.numero(), bloco.getId(), id);
        Usuario morador = buscarMorador(dto.moradorId());
        mapper.updateEntity(entity, dto, bloco, morador);
        return mapper.toResponseDTO(unidadeRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        Unidade unidade = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(unidade.getBloco().getCondominio().getId());
        if (cobrancaRepository.existsByUnidade_IdAndDeletedAtIsNull(id)) {
            throw new BusinessException("Não é permitido excluir unidade que possui cobranças vinculadas");
        }
        if (chamadoRepository.existsByUnidade_IdAndDeletedAtIsNull(id)) {
            throw new BusinessException("Não é permitido excluir unidade que possui chamados vinculados");
        }
        unidade.setDeletedAt(java.time.LocalDateTime.now());
        unidade.setDeletedBy(resolveAuditActor());
        unidadeRepository.save(unidade);
    }

    private Unidade buscarEntidade(UUID id) {
        return unidadeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    private Bloco buscarBloco(UUID id) {
        return blocoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloco não encontrado"));
    }

    private Usuario buscarMorador(UUID id) {
        if (id == null) {
            return null;
        }

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    private void validarNumeroDuplicado(String numero, UUID blocoId, UUID unidadeAtualId) {
        unidadeRepository.findByBloco_IdAndNumeroIgnoreCaseAndDeletedAtIsNull(blocoId, numero)
                .ifPresent(unidade -> {
                    if (unidadeAtualId == null || !unidade.getId().equals(unidadeAtualId)) {
                        throw new ConflictException("Já existe unidade com este número no bloco informado");
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
