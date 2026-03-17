package com.codewithus.kondo.dto.cobranca;

import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CobrancaRequestDTO(
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,
        @NotNull(message = "Vencimento é obrigatório")
        LocalDate vencimento,
        @NotNull(message = "Status é obrigatório")
        StatusCobrancaEnum status,
        @NotBlank(message = "Competencia é obrigatória")
        String competencia,
        @NotNull(message = "Unidade é obrigatória")
        UUID unidadeId
) {
}
