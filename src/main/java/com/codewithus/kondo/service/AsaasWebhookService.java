package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.webhook.AsaasPaymentWebhookRequestDTO;
import com.codewithus.kondo.dto.webhook.WebhookProcessamentoResponseDTO;

public interface AsaasWebhookService {

    WebhookProcessamentoResponseDTO processar(AsaasPaymentWebhookRequestDTO payload);
}
