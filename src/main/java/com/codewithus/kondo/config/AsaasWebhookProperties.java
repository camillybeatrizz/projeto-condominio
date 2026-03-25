package com.codewithus.kondo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kondo.integrations.asaas.webhook")
public class AsaasWebhookProperties {

    private String accessToken;
    private boolean allowLegacyAccessToken;
    private String hmacSecret;
    private String signatureHeader = "asaas-signature";
    private String timestampHeader = "asaas-timestamp";
    private long allowedTimestampSkewSeconds = 300;

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
