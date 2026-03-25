package com.codewithus.kondo.security;

import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.config.KondoSecurityProperties;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver {

    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final KondoSecurityProperties securityProperties;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioIdentityLinkService usuarioIdentityLinkService;

    public Usuario getRequiredUsuario() {
        return findCurrentUsuario()
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado não encontrado"));
    }

    public UUID getRequiredUsuarioId() {
        return getRequiredUsuario().getId();
    }

    public Optional<Usuario> findCurrentUsuario() {
        return findUsuario(authenticatedUserFacade.getRequiredSubject(), authenticatedUserFacade.getRequiredEmail());
    }

    public Optional<UUID> findCurrentUsuarioId() {
        return findCurrentUsuario().map(Usuario::getId);
    }

    public Usuario resolveRequiredUsuario(Authentication authentication) {
        return resolveUsuario(authentication)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado não encontrado"));
    }

    public Optional<Usuario> resolveUsuario(Authentication authentication) {
        return findUsuario(resolveSubject(authentication), resolveEmail(authentication));
    }

    private Optional<Usuario> findUsuario(String subject, String email) {
        Optional<Usuario> byExternalId = subject == null || subject.isBlank()
                ? Optional.empty()
                : usuarioRepository.findByExternalId(subject);

        if (byExternalId.isPresent()) {
            return byExternalId;
        }

        if (securityProperties.strictExternalIdentity()) {
            return Optional.empty();
        }

        if (subject != null && !subject.isBlank() && email != null && !email.isBlank()) {
            return usuarioRepository.findByEmail(email)
                    .flatMap(usuario -> usuarioIdentityLinkService.linkExternalIdIfEligible(usuario, subject));
        }

        if ((subject == null || subject.isBlank()) && email != null && !email.isBlank()) {
            return usuarioRepository.findByEmail(email);
        }

        return Optional.empty();
    }
    private String resolveSubject(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getToken().getSubject();
        }
        return authentication != null ? authentication.getName() : null;
    }

    private String resolveEmail(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String email = jwtAuthenticationToken.getToken().getClaimAsString("email");
            if (email != null && !email.isBlank()) {
                return email;
            }
        }

        return authentication.getName();
    }
}
