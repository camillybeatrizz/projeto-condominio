package com.codewithus.kondo.controller;

import com.codewithus.kondo.config.AsaasWebhookProperties;
import com.codewithus.kondo.dto.webhook.AsaasPaymentWebhookRequestDTO;
import com.codewithus.kondo.dto.webhook.WebhookProcessamentoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.security.AsaasWebhookSignatureVerifier;
import com.codewithus.kondo.service.AsaasWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/webhooks/asaas")
@RequiredArgsConstructor
@Tag(name = "Webhook - Asaas", description = "Recebe eventos de pagamento enviados pelo Asaas.")
public class AsaasWebhookController {

    private static final String ASAAS_ACCESS_TOKEN_HEADER = "asaas-access-token";

    private final AsaasWebhookService service;
    private final AsaasWebhookProperties properties;
    private final AsaasWebhookSignatureVerifier signatureVerifier;
    private final JsonMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Receber webhook do Asaas", description = "Valida assinatura criptografica do payload e processa eventos financeiros do Asaas.")
    public WebhookProcessamentoResponseDTO receber(
            @RequestHeader(name = ASAAS_ACCESS_TOKEN_HEADER, required = false) String accessToken,
            @RequestBody String rawPayload,
            HttpServletRequest request
    ) {
        validarCredenciais(
                rawPayload,
                request.getHeader(properties.getTimestampHeader()),
                request.getHeader(properties.getSignatureHeader()),
                accessToken
        );
        return service.processar(parsePayload(rawPayload));
    }

    private void validarCredenciais(String rawPayload, String timestamp, String signature, String accessToken) {
        if (properties.isAllowLegacyAccessToken()) {
            validarAccessToken(accessToken);
            return;
        }

        signatureVerifier.verify(rawPayload, timestamp, signature);
    }

    private void validarAccessToken(String accessToken) {
        String configuredToken = properties.getAccessToken();
        if (configuredToken == null || configuredToken.isBlank()) {
            throw new AccessDeniedException("Webhook do Asaas nao configurado");
        }

        if (accessToken == null || accessToken.isBlank()
                || !MessageDigest.isEqual(
                        configuredToken.getBytes(StandardCharsets.UTF_8),
                        accessToken.getBytes(StandardCharsets.UTF_8)
                )) {
            throw new AccessDeniedException("Token do webhook invalido");
        }
    }

    private AsaasPaymentWebhookRequestDTO parsePayload(String rawPayload) {
        try {
            return objectMapper.readValue(rawPayload, AsaasPaymentWebhookRequestDTO.class);
        } catch (JacksonException ex) {
            throw new BusinessException("Payload JSON invalido");
        }
    }
}
