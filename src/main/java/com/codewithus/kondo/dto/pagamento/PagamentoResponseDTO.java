package com.codewithus.kondo.dto.pagamento;

import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponseDTO(
        @Schema(example = "220e8400-e29b-41d4-a716-446655440012")
        UUID id,
        @Schema(example = "450.00")
        BigDecimal valor,
        @Schema(example = "2026-04-08")
        LocalDate dataPagamento,
        @Schema(example = "PIX")
        FormaPagamentoEnum forma,
        @Schema(example = "PIX-20260408-000123")
        String transactionId,
        @Schema(example = "aa0e8400-e29b-41d4-a716-446655440005")
        UUID cobrancaId,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
