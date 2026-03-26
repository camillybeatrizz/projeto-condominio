package com.codewithus.kondo.config;

import com.codewithus.kondo.security.JwtRoleConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

//Esse é um dos arquivos mais importantes da aplicação, porque ele define como o sistema protege os endpoints.
//Ela monta dois modos de funcionamento(Modo Seguro e modo aberto ou bloqueado
//Ele dita quem entra sem login, quem precisa de token, como o token é validado,como os papéis do token
// viram roles do Spring

@Configuration
// Habilita o bind da classe KondoSecurityProperties com o application.yml
@EnableConfigurationProperties(KondoSecurityProperties.class)
public class SecurityConfig {

    // Erro customizado usado quando o token JWT não possui a audience esperada
    // Isso ajuda a rejeitar tokens emitidos para outro sistema/aplicação
    private static final OAuth2Error INVALID_AUDIENCE_ERROR = new OAuth2Error(
            "invalid_token",
            "The required JWT audience is missing",
            null
    );

    // Cadeia de filtros usada quando a segurança está habilitada:
    // kondo.security.enabled=true
    @Bean
    @ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "true")
    public SecurityFilterChain securedFilterChain(HttpSecurity http, JwtRoleConverter jwtRoleConverter) throws Exception {
        return http

                // Desabilita CSRF
                // Em APIs REST com JWT isso é comum, porque a autenticação não depende de sessão/cookie
                .csrf(AbstractHttpConfigurer::disable)
                // Define as regras de autorização das requisições HTTP
                .authorizeHttpRequests(auth -> auth
                        // Esses endpoints ficam liberados sem autenticação
                        .requestMatchers(
                                "/error",
                                "/actuator/health",
                                "/webhooks/asaas",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2

                        // Define um conversor customizado para transformar claims do JWT em authorities/roles do Spring
                        // Ex: converter roles do Keycloak para ROLE_ADMIN, ROLE_SINDICO etc.
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtRoleConverter))
                )
                // Finaliza e constrói o SecurityFilterChain
                .build();
    }

    // Bean responsável por validar e decodificar o JWT
    @Bean
    @ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "true")
    public JwtDecoder jwtDecoder(KondoSecurityProperties securityProperties,
                                 org.springframework.core.env.Environment environment) {

        // Lê do application.yml/properties a URL do issuer
        // Ex: servidor do Keycloak/Pinniped que emitiu o token
        String issuerUri = environment.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");

        // Lê a URL do conjunto de chaves públicas (JWK Set)
        // Essas chaves são usadas para validar a assinatura do token
        String jwkSetUri = environment.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri");

        // Cria o decoder JWT:
        // - se tiver jwk-set-uri, usa diretamente essa URL
        // - senão, tenta descobrir com base no issuer
        NimbusJwtDecoder jwtDecoder = jwkSetUri != null && !jwkSetUri.isBlank()
                ? NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
                : NimbusJwtDecoder.withIssuerLocation(issuerUri).build();

        // Validador padrão com issuer:
        // garante que o token foi emitido pelo provedor esperado
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = jwt -> hasExpectedAudience(jwt, securityProperties.audiences())
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(INVALID_AUDIENCE_ERROR);

        // Junta os validadores:
        // o token só será aceito se passar em todos
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        return jwtDecoder;
    }

    // Cadeia de filtros usada quando a segurança está desabilitada:
    // kondo.security.enabled=false
    // ou quando a propriedade nem foi definida (matchIfMissing = true)
    @Bean
    @ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "false", matchIfMissing = true)
    public SecurityFilterChain openFilterChain(HttpSecurity http, KondoSecurityProperties securityProperties) throws Exception {

        // Mesmo com segurança desabilitada, o sistema ainda pode negar tudo,
        // caso allowInsecureOpenAccess=false
        if (!securityProperties.allowInsecureOpenAccess()) {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth

                            // Só libera endpoints mínimos de infraestrutura
                            .requestMatchers(
                                    "/error",
                                    "/actuator/health"
                            ).permitAll()

                            //Todo o resto fica bloqueado
                            .anyRequest().denyAll()
                    )
                    .build();
        }

        // Se allowInsecureOpenAccess=true:
        // libera tudo sem autenticação
        // útil para desenvolvimento/testes, mas perigoso em produção
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    // Metodo auxiliar para validar se o token possui uma audience esperada
    private boolean hasExpectedAudience(Jwt jwt, List<String> expectedAudiences) {

        // Se nenhuma audience foi configurada no sistema,
        // não faz essa validação e aceita o token
        if (expectedAudiences == null || expectedAudiences.isEmpty()) {
            return true;
        }

        // Lê as audiences presentes no token
        List<String> tokenAudiences = jwt.getAudience();

        // Retorna true se ao menos uma audience do token estiver entre as esperadas
        return tokenAudiences != null && tokenAudiences.stream().anyMatch(expectedAudiences::contains);
    }
}
