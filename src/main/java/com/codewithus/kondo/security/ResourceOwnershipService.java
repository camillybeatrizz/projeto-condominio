package com.codewithus.kondo.security;

import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.ChamadoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component("resourceOwnershipService")
@RequiredArgsConstructor
public class ResourceOwnershipService {

    private final AcessoRepository acessoRepository;
    private final CurrentUserResolver currentUserResolver;
    private final CondominioRepository condominioRepository;
    private final BlocoRepository blocoRepository;
    private final UnidadeRepository unidadeRepository;
    private final CobrancaRepository cobrancaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ChamadoRepository chamadoRepository;

    public boolean canAccessCondominio(Authentication authentication, UUID condominioId) {
        return (hasAdministrativeAccess(authentication) && condominioRepository.findByIdAndDeletedAtIsNull(condominioId).isPresent())
                || currentUserResolver.resolveUsuario(authentication)
                .map(usuario -> canAccessCondominio(usuario.getId(), condominioId))
                .orElse(false);
    }

    public boolean canAccessBloco(Authentication authentication, UUID blocoId) {
        if (hasAdministrativeAccess(authentication)) {
            return blocoRepository.findByIdAndDeletedAtIsNull(blocoId).isPresent();
        }

        return blocoRepository.findByIdAndDeletedAtIsNull(blocoId)
                .flatMap(bloco -> currentUserResolver.resolveUsuario(authentication)
                        .map(usuario -> canAccessCondominio(usuario.getId(), bloco.getCondominio().getId())))
                .orElse(false);
    }

    public boolean canAccessUnidade(Authentication authentication, UUID unidadeId) {
        if (hasAdministrativeAccess(authentication)) {
            return unidadeRepository.findByIdAndDeletedAtIsNull(unidadeId).isPresent();
        }

        return unidadeRepository.findByIdAndDeletedAtIsNull(unidadeId)
                .flatMap(unidade -> currentUserResolver.resolveUsuario(authentication)
                        .map(usuario -> unidadeRepository.existsByIdAndMorador_Id(unidadeId, usuario.getId())
                                || canAccessCondominio(usuario.getId(), unidade.getBloco().getCondominio().getId())))
                .orElse(false);
    }

    public boolean canAccessCobranca(Authentication authentication, UUID cobrancaId) {
        if (hasAdministrativeAccess(authentication)) {
            return true;
        }

        return cobrancaRepository.findByIdAndDeletedAtIsNull(cobrancaId)
                .flatMap(cobranca -> currentUserResolver.resolveUsuario(authentication)
                        .map(usuario -> cobrancaRepository.existsByIdAndDeletedAtIsNullAndUnidade_Morador_Id(cobrancaId, usuario.getId())
                                || canAccessCondominio(usuario.getId(), cobranca.getUnidade().getBloco().getCondominio().getId())))
                .orElse(false);
    }

    public boolean canAccessPagamento(Authentication authentication, UUID pagamentoId) {
        if (hasAdministrativeAccess(authentication)) {
            return true;
        }

        return pagamentoRepository.findByIdAndDeletedAtIsNull(pagamentoId)
                .flatMap(pagamento -> currentUserResolver.resolveUsuario(authentication)
                        .map(usuario -> pagamentoRepository.existsByIdAndDeletedAtIsNullAndCobranca_Unidade_Morador_Id(pagamentoId, usuario.getId())
                                || canAccessCondominio(usuario.getId(), pagamento.getCobranca().getUnidade().getBloco().getCondominio().getId())))
                .orElse(false);
    }

    public boolean canAccessChamado(Authentication authentication, UUID chamadoId) {
        if (hasAdministrativeAccess(authentication)) {
            return true;
        }

        return chamadoRepository.findByIdAndDeletedAtIsNull(chamadoId)
                .flatMap(chamado -> currentUserResolver.resolveUsuario(authentication)
                        .map(usuario -> chamadoRepository.existsByIdAndDeletedAtIsNullAndUnidade_Morador_Id(chamadoId, usuario.getId())
                                || canAccessCondominio(usuario.getId(), chamado.getUnidade().getBloco().getCondominio().getId())))
                .orElse(false);
    }

    public boolean canCreateChamado(Authentication authentication, UUID unidadeId) {
        if (hasAdministrativeAccess(authentication)) {
            return unidadeRepository.findByIdAndDeletedAtIsNull(unidadeId).isPresent();
        }

        return unidadeRepository.findByIdAndDeletedAtIsNull(unidadeId)
                .flatMap(unidade -> currentUserResolver.resolveUsuario(authentication)
                        .map(usuario -> unidadeRepository.existsByIdAndMorador_Id(unidadeId, usuario.getId())
                                || canAccessCondominio(usuario.getId(), unidade.getBloco().getCondominio().getId())))
                .orElse(false);
    }

    public boolean canAccessCondominio(UUID usuarioId, UUID condominioId) {
        return condominioRepository.findByIdAndDeletedAtIsNull(condominioId).isPresent()
                && (acessoRepository.existsByUsuario_IdAndCondominio_Id(usuarioId, condominioId)
                || unidadeRepository.existsByMorador_IdAndBloco_Condominio_Id(usuarioId, condominioId));
    }

    public List<UUID> findCondominioIdsByUsuarioId(UUID usuarioId) {
        return Stream.concat(
                        acessoRepository.findDistinctCondominioIdsByUsuario_Id(usuarioId).stream(),
                        unidadeRepository.findDistinctCondominioIdsByMorador_Id(usuarioId).stream()
                )
                .distinct()
                .toList();
    }

    private boolean hasAdministrativeAccess(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
