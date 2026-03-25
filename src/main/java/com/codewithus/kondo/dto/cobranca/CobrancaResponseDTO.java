package com.codewithus.kondo.dto.cobranca;

import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CobrancaResponseDTO(
        @Schema(example = "aa0e8400-e29b-41d4-a716-446655440005")
        UUID id,
        @Schema(example = "450.00")
        BigDecimal valor,
        @Schema(example = "2026-04-10")
        LocalDate vencimento,
        @Schema(example = "PENDENTE")
        StatusCobrancaEnum status,
        @Schema(example = "2026-04")
        String competencia,
        @Schema(example = "pay_000123456")
        String referenciaExterna,
        @Schema(example = "https://sandbox.asaas.com/i/pay_000123456")
        String urlPagamentoExterno,
        @Schema(example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
