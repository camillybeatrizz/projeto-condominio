package com.codewithus.kondo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

//Esse arquivo define as regras de segurança da aplicação inteira.

// @ConfigurationProperties → faz o bind automático com o application.yml
// prefix = "kondo.security" → tudo que estiver nesse caminho será mapeado aqui
@ConfigurationProperties(prefix = "kondo.security")

// record → classe imutável (não tem setters, só getters automáticos)
// ideal para configuração: evita alterações em runtime
public record KondoSecurityProperties(

        // Ativa ou desativa a segurança da aplicação
        // Ex: validar JWT, autenticação, etc.
        boolean enabled,

        // Permite acesso aberto SEM autenticação (modo inseguro)
        // usado geralmente em DEV ou testes
        boolean allowInsecureOpenAccess,

        // ID do cliente (geralmente do Keycloak / OAuth2)
        // identifica sua aplicação no servidor de autenticação
        String clientId,

        // Nome do campo no token (JWT) que representa o usuário
        // Ex: "sub", "preferred_username", "email"
        String principalClaim,

        // Lista de audiences válidas do token (aud)
        // garante que o token foi emitido para esta aplicação
        List<String> audiences,

        // Se true → exige identidade externa válida (ex: vindo do Keycloak)
        // se false → pode aceitar usuários locais ou fallback
        boolean strictExternalIdentity
) {
}
