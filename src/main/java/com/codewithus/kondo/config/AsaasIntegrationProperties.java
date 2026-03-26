package com.codewithus.kondo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*serve para ler configurações do application.yml/properties e disponibilizar isso no código.
Ou seja: ela conecta o Spring → arquivo de configuração → código Java*/

// @Component → registra essa classe como um bean no Spring (gerenciado automaticamente)
@Component
// @ConfigurationProperties → faz o bind automático das propriedades do application.yml
// prefix = "kondo.integrations.asaas" → tudo que começar com isso será mapeado aqui
@ConfigurationProperties(prefix = "kondo.integrations.asaas")
public class AsaasIntegrationProperties {

    private boolean enabled;    // Flag para ativar/desativar a integração com o Asaas
    private String apiKey;   // Chave da API (vem do application.yml ou variável de ambiente)

    // URL base da API do Asaas
    // já possui valor padrão (sandbox), caso não seja definido no config
    private String baseUrl = "https://api-sandbox.asaas.com/v3";

    // Getter do enabled (Spring usa isso internamente também)
    public boolean isEnabled() {
        return enabled;
    }

    // Setter do enabled → Spring injeta o valor vindo do application.yml aqui
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiKey() {
        return apiKey;
    }

    //valor vindo do config externo
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    //permite sobrescrever a URL padrão via config
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
