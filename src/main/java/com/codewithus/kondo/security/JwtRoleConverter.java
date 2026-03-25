package com.codewithus.kondo.security;

import com.codewithus.kondo.config.KondoSecurityProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final KondoSecurityProperties securityProperties;
    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public JwtRoleConverter(KondoSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>(defaultAuthoritiesConverter.convert(jwt));

        extractRealmRoles(jwt).forEach(role -> authorities.add(toRole(role)));
        extractClientRoles(jwt).forEach(role -> authorities.add(toRole(role)));
        extractGenericRoles(jwt).forEach(role -> authorities.add(toRole(role)));
        extractGroups(jwt).forEach(group -> {
            authorities.add(toGroup(group));
            String applicationRole = extractApplicationRole(group);
            if (applicationRole != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + applicationRole));
            }
        });

        String principalClaim = resolvePrincipalClaim(jwt);
        return new JwtAuthenticationToken(jwt, authorities, principalClaim);
    }

    private Collection<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return List.of();
        }

        Object roles = realmAccess.get("roles");
        return asStringCollection(roles);
    }

    private Collection<String> extractClientRoles(Jwt jwt) {
        String clientId = securityProperties.clientId();
        if (clientId == null || clientId.isBlank()) {
            return List.of();
        }

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return List.of();
        }

        Object clientAccess = resourceAccess.get(clientId);
        if (!(clientAccess instanceof Map<?, ?> clientMap)) {
            return List.of();
        }

        return asStringCollection(clientMap.get("roles"));
    }

    private Collection<String> extractGenericRoles(Jwt jwt) {
        return asStringCollection(jwt.getClaims().get("roles"));
    }

    private Collection<String> extractGroups(Jwt jwt) {
        return asStringCollection(jwt.getClaims().get("groups"));
    }

    private Collection<String> asStringCollection(Object value) {
        if (!(value instanceof Collection<?> collection)) {
            return List.of();
        }

        return collection.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .filter(s -> !s.isBlank())
                .toList();
    }

    private String resolvePrincipalClaim(Jwt jwt) {
        String principalClaim = securityProperties.principalClaim();
        if (principalClaim != null && !principalClaim.isBlank()) {
            String claimValue = jwt.getClaimAsString(principalClaim);
            if (claimValue != null && !claimValue.isBlank()) {
                return claimValue;
            }
        }

        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isBlank()) {
            return email;
        }

        String preferredUsername = jwt.getClaimAsString("preferred_username");
        if (preferredUsername != null && !preferredUsername.isBlank()) {
            return preferredUsername;
        }

        String username = jwt.getClaimAsString("username");
        if (username != null && !username.isBlank()) {
            return username;
        }

        return jwt.getSubject();
    }

    private String extractApplicationRole(String value) {
        String normalized = normalize(value);
        if (normalized.equals("ADMIN") || normalized.equals("SINDICO") || normalized.equals("MORADOR")) {
            return normalized;
        }

        if (normalized.contains("_")) {
            String[] parts = normalized.split("_");
            String lastPart = parts[parts.length - 1];
            if (lastPart.equals("ADMIN") || lastPart.equals("SINDICO") || lastPart.equals("MORADOR")) {
                return lastPart;
            }
        }

        return null;
    }

    private GrantedAuthority toRole(String value) {
        return new SimpleGrantedAuthority("ROLE_" + normalize(value));
    }

    private GrantedAuthority toGroup(String value) {
        return new SimpleGrantedAuthority("GROUP_" + normalize(value));
    }

    private String normalize(String value) {
        return value.trim()
                .replace('-', '_')
                .replace(' ', '_')
                .replace('/', '_')
                .toUpperCase(Locale.ROOT);
    }
}
