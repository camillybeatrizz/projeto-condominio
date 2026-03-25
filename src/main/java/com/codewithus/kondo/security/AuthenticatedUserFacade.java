package com.codewithus.kondo.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserFacade {

    public boolean isAuthenticated() {
        return getAuthentication() != null;
    }

    public boolean isAdmin() {
        Authentication authentication = getAuthentication();
        return hasAuthority(authentication, "ROLE_ADMIN");
    }

    public boolean isSindico() {
        Authentication authentication = getAuthentication();
        return hasAuthority(authentication, "ROLE_SINDICO");
    }

    public boolean isMorador() {
        Authentication authentication = getAuthentication();
        return hasAuthority(authentication, "ROLE_MORADOR");
    }

    public String getRequiredEmail() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Usuário autenticado não encontrado");
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String email = jwtAuthenticationToken.getToken().getClaimAsString("email");
            if (email != null && !email.isBlank()) {
                return email;
            }
        }

        if (authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalStateException("Usuário autenticado não encontrado");
        }

        return authentication.getName();
    }

    public String getRequiredSubject() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Usuário autenticado não encontrado");
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            String subject = jwtAuthenticationToken.getToken().getSubject();
            if (subject != null && !subject.isBlank()) {
                return subject;
            }
        }

        if (authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalStateException("Usuário autenticado não encontrado");
        }

        return authentication.getName();
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return authentication;
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }
}
