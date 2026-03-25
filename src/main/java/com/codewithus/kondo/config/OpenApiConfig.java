package com.codewithus.kondo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kondoOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Kondo API")
                        .version("v1")
                        .description("""
                                API REST para gestao condominial do projeto Kondo.

                                Esta documentacao foi organizada para facilitar o entendimento funcional dos endpoints:
                                - recursos estruturais, como condominios, blocos e unidades
                                - recursos financeiros, como cobrancas, pagamentos, despesas e contas bancarias
                                - recursos administrativos, como usuarios, acessos, contratos e fornecedores

                                Perfis esperados:
                                - ADMIN: administracao global da plataforma, usuarios e acessos
                                - SINDICO: gestao estrutural, operacional e financeira do condominio
                                - MORADOR: consulta dados proprios, acompanha cobrancas e pagamentos e abre chamados da propria unidade

                                Como interpretar esta documentacao:
                                - cada endpoint informa quem pode executar a operacao
                                - os exemplos de request e response mostram payloads realistas
                                - os codigos 401 e 403 diferenciam falta de autenticacao de falta de permissao
                                - os recursos estruturais podem permitir leitura por MORADOR quando servem de apoio a navegacao e consultas vinculadas a propria unidade

                                Execucao local:
                                - para subir a API com banco em memoria, use o profile local
                                - comando sugerido: ./mvnw spring-boot:run -Dspring-boot.run.profiles=local

                                Autenticacao:
                                - em ambiente local, a seguranca pode estar desabilitada
                                - em ambientes com seguranca habilitada, use um token JWT Bearer no botao Authorize do Swagger
                                - o token deve ser emitido pelo provedor configurado, como Keycloak ou Pinniped
                                """)
                        .contact(new Contact()
                                .name("Equipe Kondo"))
                        .license(new License()
                                .name("Uso academico e demonstrativo")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        Autenticacao por token Bearer JWT.

                                        Como usar:
                                        1. clique em Authorize no Swagger UI
                                        2. informe o token no formato Bearer
                                        3. execute os endpoints conforme as permissoes do perfil autenticado

                                        Em ambiente local com o profile local, a seguranca pode estar desabilitada para facilitar testes e estudo da API.
                                        """)));
    }
}
