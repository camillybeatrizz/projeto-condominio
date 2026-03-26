package com.codewithus.kondo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

//Esse arquivo liga a segurança dentro dos métodos do sistema.
//O sistema tem 2 níveis ded segurança: Segurança de requisição (HTTP) e Segurança por metodo(esse arquivo).

// @Configuration → classe de configuração do Spring (define comportamento da aplicação)
@Configuration

// @EnableMethodSecurity → ativa segurança baseada em anotações nos métodos
// Permite usar: @PreAuthorize("hasRole('ADMIN')") ,@PostAuthorize, @Secured ,etc.
@EnableMethodSecurity

// @ConditionalOnProperty → só ativa essa configuração se a propriedade estiver habilitada
// prefix = "kondo.security"
// name = "enabled"
// havingValue = "true"

// Ou seja:
// kondo.security.enabled=true → ativa segurança por método
// kondo.security.enabled=false → ignora essa configuração
@ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "true")
public class MethodSecurityConfig {
}
