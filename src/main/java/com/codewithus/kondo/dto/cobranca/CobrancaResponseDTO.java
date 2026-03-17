package com.codewithus.kondo.dto.cobranca;

import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CobrancaResponseDTO(
        UUID id,
        BigDecimal valor,
        LocalDate vencimento,
        StatusCobrancaEnum status,
        String competencia,
        UUID unidadeId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
