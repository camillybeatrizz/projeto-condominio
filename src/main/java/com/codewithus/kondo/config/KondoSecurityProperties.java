package com.codewithus.kondo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "kondo.security")
public record KondoSecurityProperties(
        boolean enabled,
        boolean allowInsecureOpenAccess,
        String clientId,
        String principalClaim,
        List<String> audiences,
        boolean strictExternalIdentity
) {
}
