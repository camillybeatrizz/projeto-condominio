package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.enums.PerfilEnum;
import com.codewithus.kondo.dto.acesso.AcessoRequestDTO;
import com.codewithus.kondo.dto.acesso.AcessoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.AcessoMapper;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.service.AuditoriaService;
import com.codewithus.kondo.service.AcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AcessoServiceImpl implements AcessoService {

    private static final String ENTIDADE_ACESSO = "ACESSO";

    private final AcessoRepository acessoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CondominioRepository condominioRepository;
    private final UnidadeRepository unidadeRepository;
    private final AcessoMapper mapper;
    private final AuditoriaService auditoriaService;
    private final AuthenticatedUserFacade authenticatedUserFacade;

    @Override
    public AcessoResponseDTO salvar(AcessoRequestDTO dto) {
        Usuario usuario = buscarUsuario(dto.usuarioId());
        Condominio condominio = buscarCondominio(dto.condominioId());
        Unidade unidade = buscarUnidade(dto.unidadeId());

        validarRegras(dto, usuario, condominio, unidade, null);

        Acesso entity = mapper.toEntity(dto, usuario, condominio, unidade);
        entity = acessoRepository.save(entity);
        sincronizarVinculoMorador(null, entity);
        registrarAuditoria("ACESSO_CRIADO", entity, "Acesso criado para o usuario " + usuario.getId());
        return mapper.toResponseDTO(entity);
    }

    @Override
    public AcessoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<AcessoResponseDTO> listar() {
        return acessoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public AcessoResponseDTO atualizar(UUID id, AcessoRequestDTO dto) {
        Acesso entity = buscarEntidade(id);
        Usuario usuario = buscarUsuario(dto.usuarioId());
        Condominio condominio = buscarCondominio(dto.condominioId());
        Unidade unidade = buscarUnidade(dto.unidadeId());

        validarRegras(dto, usuario, condominio, unidade, id);

        Acesso snapshotAnterior = copiarEstado(entity);
        mapper.updateEntity(entity, dto, usuario, condominio, unidade);
        entity = acessoRepository.save(entity);
        sincronizarVinculoMorador(snapshotAnterior, entity);
        registrarAuditoria("ACESSO_ATUALIZADO", entity, "Acesso atualizado para o usuario " + usuario.getId());
        return mapper.toResponseDTO(entity);
    }

    @Override
    public void deletar(UUID id) {
        Acesso entity = buscarEntidade(id);
        desvincularUnidadeAnterior(entity);
        acessoRepository.delete(entity);
        registrarAuditoria("ACESSO_EXCLUIDO", entity, "Acesso removido");
    }

    private Acesso buscarEntidade(UUID id) {
        return acessoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acesso não encontrado"));
    }

    private Usuario buscarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    private Condominio buscarCondominio(UUID id) {
        if (id == null) {
            return null;
        }

        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private Unidade buscarUnidade(UUID id) {
        if (id == null) {
            return null;
        }

        return unidadeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    private void validarRegras(AcessoRequestDTO dto, Usuario usuario, Condominio condominio, Unidade unidade, UUID acessoAtualId) {
        validarEscopoPorPerfil(dto.perfil(), condominio, unidade);
        validarRelacaoEntreCondominioEUnidade(condominio, unidade);
        validarDuplicidadeDeAcesso(usuario.getId(), dto.perfil(), condominio, acessoAtualId);
        validarDisponibilidadeDaUnidade(dto.perfil(), unidade, usuario.getId(), acessoAtualId);
    }

    private void validarEscopoPorPerfil(PerfilEnum perfil, Condominio condominio, Unidade unidade) {
        if (perfil == PerfilEnum.ADMIN) {
            if (condominio != null || unidade != null) {
                throw new BusinessException("Perfil ADMIN não deve ser vinculado a condomínio ou unidade");
            }
            return;
        }

        if (condominio == null) {
            throw new BusinessException("Condominio é obrigatório para perfis SINDICO e MORADOR");
        }

        if (perfil == PerfilEnum.SINDICO && unidade != null) {
            throw new BusinessException("Perfil SINDICO não deve ser vinculado a unidade");
        }

        if (perfil == PerfilEnum.MORADOR && unidade == null) {
            throw new BusinessException("Unidade é obrigatória para perfil MORADOR");
        }
    }

    private void validarRelacaoEntreCondominioEUnidade(Condominio condominio, Unidade unidade) {
        if (unidade == null) {
            return;
        }

        UUID condominioDaUnidade = unidade.getBloco() != null && unidade.getBloco().getCondominio() != null
                ? unidade.getBloco().getCondominio().getId()
                : null;

        if (condominio == null || condominioDaUnidade == null || !condominioDaUnidade.equals(condominio.getId())) {
            throw new BusinessException("A unidade informada não pertence ao condomínio selecionado");
        }
    }

    private void validarDuplicidadeDeAcesso(UUID usuarioId, PerfilEnum perfil, Condominio condominio, UUID acessoAtualId) {
        boolean duplicado = condominio == null
                ? acessoRepository.existsByUsuario_IdAndPerfilAndCondominioIsNull(usuarioId, perfil)
                : acessoRepository.existsByUsuario_IdAndPerfilAndCondominio_Id(usuarioId, perfil, condominio.getId());

        if (!duplicado) {
            return;
        }

        if (acessoAtualId != null) {
            Acesso acessoAtual = buscarEntidade(acessoAtualId);
            UUID condominioAtualId = acessoAtual.getCondominio() != null ? acessoAtual.getCondominio().getId() : null;
            if (acessoAtual.getUsuario().getId().equals(usuarioId)
                    && acessoAtual.getPerfil() == perfil
                    && ((condominioAtualId == null && condominio == null)
                    || (condominioAtualId != null && condominio != null && condominioAtualId.equals(condominio.getId())))) {
                return;
            }
        }

        throw new ConflictException("Já existe acesso com este perfil para o usuário no escopo informado");
    }

    private void validarDisponibilidadeDaUnidade(PerfilEnum perfil, Unidade unidade, UUID usuarioId, UUID acessoAtualId) {
        if (perfil != PerfilEnum.MORADOR || unidade == null) {
            return;
        }

        acessoRepository.findByUnidade_IdAndPerfil(unidade.getId(), PerfilEnum.MORADOR)
                .ifPresent(acessoExistente -> {
                    if (acessoAtualId == null || !acessoExistente.getId().equals(acessoAtualId)) {
                        throw new ConflictException("A unidade informada já está vinculada a outro acesso de morador");
                    }
                });

        if (unidade.getMorador() != null) {
            UUID moradorAtualId = unidade.getMorador().getId();
            Acesso acessoAtual = acessoAtualId != null ? buscarEntidade(acessoAtualId) : null;
            UUID usuarioAtualId = acessoAtual != null && acessoAtual.getUsuario() != null ? acessoAtual.getUsuario().getId() : null;

            if (!moradorAtualId.equals(usuarioId) && acessoAtualId == null) {
                throw new ConflictException("A unidade informada já possui um morador vinculado");
            }

            if (acessoAtualId != null && (usuarioAtualId == null || !moradorAtualId.equals(usuarioAtualId))) {
                throw new ConflictException("A unidade informada já possui um morador vinculado");
            }
        }
    }

    private void sincronizarVinculoMorador(Acesso acessoAnterior, Acesso acessoAtual) {
        desvincularUnidadeAnterior(acessoAnterior);

        if (acessoAtual.getPerfil() != PerfilEnum.MORADOR || acessoAtual.getUnidade() == null) {
            return;
        }

        Unidade unidade = acessoAtual.getUnidade();
        unidade.setMorador(acessoAtual.getUsuario());
        unidadeRepository.save(unidade);
    }

    private void desvincularUnidadeAnterior(Acesso acesso) {
        if (acesso == null || acesso.getPerfil() != PerfilEnum.MORADOR || acesso.getUnidade() == null) {
            return;
        }

        Unidade unidade = acesso.getUnidade();
        if (unidade.getMorador() != null && acesso.getUsuario() != null
                && unidade.getMorador().getId().equals(acesso.getUsuario().getId())) {
            unidade.setMorador(null);
            unidadeRepository.save(unidade);
        }
    }

    private Acesso copiarEstado(Acesso entity) {
        Acesso copia = new Acesso();
        copia.setId(entity.getId());
        copia.setUsuario(entity.getUsuario());
        copia.setCondominio(entity.getCondominio());
        copia.setUnidade(entity.getUnidade());
        copia.setPerfil(entity.getPerfil());
        return copia;
    }

    private void registrarAuditoria(String tipoEvento, Acesso acesso, String detalhe) {
        String ator = authenticatedUserFacade.isAuthenticated() ? authenticatedUserFacade.getRequiredSubject() : "SYSTEM";
        auditoriaService.registrar(tipoEvento, ENTIDADE_ACESSO, acesso.getId(), ator, detalhe);
    }
}
