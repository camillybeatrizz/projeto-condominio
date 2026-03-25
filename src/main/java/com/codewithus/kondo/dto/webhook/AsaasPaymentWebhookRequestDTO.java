package com.codewithus.kondo.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsaasPaymentWebhookRequestDTO(
        String id,
        String event,
        PaymentData payment
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PaymentData(
            String id,
            String paymentLink,
            BigDecimal value,
            String billingType,
            LocalDate clientPaymentDate
    ) {
    }
}
