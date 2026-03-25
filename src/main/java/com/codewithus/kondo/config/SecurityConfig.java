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

@Configuration
@EnableConfigurationProperties(KondoSecurityProperties.class)
public class SecurityConfig {

    private static final OAuth2Error INVALID_AUDIENCE_ERROR = new OAuth2Error(
            "invalid_token",
            "The required JWT audience is missing",
            null
    );

    @Bean
    @ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "true")
    public SecurityFilterChain securedFilterChain(HttpSecurity http, JwtRoleConverter jwtRoleConverter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/error",
                                "/actuator/health",
                                "/webhooks/asaas",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtRoleConverter))
                )
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "true")
    public JwtDecoder jwtDecoder(KondoSecurityProperties securityProperties,
                                 org.springframework.core.env.Environment environment) {
        String issuerUri = environment.getProperty("spring.security.oauth2.resourceserver.jwt.issuer-uri");
        String jwkSetUri = environment.getProperty("spring.security.oauth2.resourceserver.jwt.jwk-set-uri");

        NimbusJwtDecoder jwtDecoder = jwkSetUri != null && !jwkSetUri.isBlank()
                ? NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build()
                : NimbusJwtDecoder.withIssuerLocation(issuerUri).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = jwt -> hasExpectedAudience(jwt, securityProperties.audiences())
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(INVALID_AUDIENCE_ERROR);

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        return jwtDecoder;
    }

    @Bean
    @ConditionalOnProperty(prefix = "kondo.security", name = "enabled", havingValue = "false", matchIfMissing = true)
    public SecurityFilterChain openFilterChain(HttpSecurity http, KondoSecurityProperties securityProperties) throws Exception {
        if (!securityProperties.allowInsecureOpenAccess()) {
            return http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/error",
                                    "/actuator/health"
                            ).permitAll()
                            .anyRequest().denyAll()
                    )
                    .build();
        }

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    private boolean hasExpectedAudience(Jwt jwt, List<String> expectedAudiences) {
        if (expectedAudiences == null || expectedAudiences.isEmpty()) {
            return true;
        }

        List<String> tokenAudiences = jwt.getAudience();
        return tokenAudiences != null && tokenAudiences.stream().anyMatch(expectedAudiences::contains);
    }
}
