package com.codewithus.kondo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// @Component → registra essa classe como um bean no Spring (gerenciado automaticamente)
@Component
// Mapeia propriedades do application.yml com prefixo:
// kondo.integrations.asaas.webhook
@ConfigurationProperties(prefix = "kondo.integrations.asaas.webhook")
public class AsaasWebhookProperties {

    // Token simples para autenticação (modo mais básico)
    // Usado quando o Asaas envia webhook com um header/token fixo
    private String accessToken;

    // Permite aceitar o modelo antigo de autenticação (legado)
    // útil durante migração de segurança
    private boolean allowLegacyAccessToken;

    // Segredo usado para validar assinatura HMAC
    // (forma mais segura de validar que o webhook veio do Asaas)
    private String hmacSecret;

    // Nome do header onde vem a assinatura da requisição
    // Ex: "asaas-signature"
    private String signatureHeader = "asaas-signature";

    // Nome do header onde vem o timestamp da requisição
    // usado para evitar ataques de replay
    private String timestampHeader = "asaas-timestamp";

    // Tempo máximo permitido (em segundos) entre envio e recebimento
    // Ex: 300s = 5 minutos
    // evita que alguém reutilize uma requisição antiga (replay attack)
    private long allowedTimestampSkewSeconds = 300;

    //Getters e Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isAllowLegacyAccessToken() {
        return allowLegacyAccessToken;
    }

    public void setAllowLegacyAccessToken(boolean allowLegacyAccessToken) {
        this.allowLegacyAccessToken = allowLegacyAccessToken;
    }

    public String getHmacSecret() {
        return hmacSecret;
    }

    public void setHmacSecret(String hmacSecret) {
        this.hmacSecret = hmacSecret;
    }

    public String getSignatureHeader() {
        return signatureHeader;
    }

    public void setSignatureHeader(String signatureHeader) {
        this.signatureHeader = signatureHeader;
    }

    public String getTimestampHeader() {
        return timestampHeader;
    }

    public void setTimestampHeader(String timestampHeader) {
        this.timestampHeader = timestampHeader;
    }

    public long getAllowedTimestampSkewSeconds() {
        return allowedTimestampSkewSeconds;
    }

    public void setAllowedTimestampSkewSeconds(long allowedTimestampSkewSeconds) {
        this.allowedTimestampSkewSeconds = allowedTimestampSkewSeconds;
    }
}
