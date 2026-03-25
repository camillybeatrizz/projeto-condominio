package com.codewithus.kondo.dto.integration;

import java.time.LocalDateTime;

public record AsaasPixQrCodeResult(
        String encodedImage,
        String payload,
        LocalDateTime expirationDate
) {
}
