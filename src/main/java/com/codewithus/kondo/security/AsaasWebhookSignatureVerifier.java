package com.codewithus.kondo.security;

import com.codewithus.kondo.config.AsaasWebhookProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class AsaasWebhookSignatureVerifier {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private final AsaasWebhookProperties properties;
    private final Clock clock = Clock.systemUTC();

    public void verify(String rawPayload, String timestampHeaderValue, String signatureHeaderValue) {
        String secret = properties.getHmacSecret();
        if (secret == null || secret.isBlank()) {
            throw new AccessDeniedException("Assinatura do webhook nao configurada");
        }

        Instant timestamp = parseTimestamp(timestampHeaderValue);
        validateFreshness(timestamp);

        if (signatureHeaderValue == null || signatureHeaderValue.isBlank()) {
            throw new AccessDeniedException("Assinatura do webhook ausente");
        }

        String providedSignature = normalizeSignature(signatureHeaderValue);
        byte[] expectedSignature = sign(timestampHeaderValue.trim() + "." + rawPayload, secret);
        byte[] receivedSignature = decodeHex(providedSignature);

        if (!MessageDigest.isEqual(expectedSignature, receivedSignature)) {
            throw new AccessDeniedException("Assinatura do webhook invalida");
        }
    }

    private Instant parseTimestamp(String timestampHeaderValue) {
        if (timestampHeaderValue == null || timestampHeaderValue.isBlank()) {
            throw new AccessDeniedException("Timestamp do webhook ausente");
        }

        String normalized = timestampHeaderValue.trim();
        try {
            if (normalized.chars().allMatch(Character::isDigit)) {
                return Instant.ofEpochSecond(Long.parseLong(normalized));
            }
            return Instant.parse(normalized);
        } catch (DateTimeParseException | NumberFormatException ex) {
            throw new AccessDeniedException("Timestamp do webhook invalido");
        }
    }

    private void validateFreshness(Instant timestamp) {
        long allowedSkewSeconds = Math.max(0, properties.getAllowedTimestampSkewSeconds());
        long delta = Math.abs(Instant.now(clock).getEpochSecond() - timestamp.getEpochSecond());
        if (delta > allowedSkewSeconds) {
            throw new AccessDeniedException("Timestamp do webhook expirado");
        }
    }

    private String normalizeSignature(String signatureHeaderValue) {
        String normalized = signatureHeaderValue.trim();
        if (normalized.regionMatches(true, 0, "sha256=", 0, "sha256=".length())) {
            return normalized.substring("sha256=".length());
        }
        return normalized;
    }

    private byte[] decodeHex(String signature) {
        try {
            return HexFormat.of().parseHex(signature);
        } catch (IllegalArgumentException ex) {
            throw new AccessDeniedException("Assinatura do webhook invalida");
        }
    }

    private byte[] sign(String content, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException ex) {
            throw new AccessDeniedException("Nao foi possivel validar a assinatura do webhook");
        }
    }
}
