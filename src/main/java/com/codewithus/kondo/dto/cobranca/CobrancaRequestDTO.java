package com.codewithus.kondo.dto.cobranca;

import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CobrancaRequestDTO(
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        @Schema(description = "Valor total da cobranca.", example = "450.00")
        BigDecimal valor,
        @NotNull(message = "Vencimento é obrigatório")
        @Schema(description = "Data de vencimento da cobranca.", example = "2026-04-10")
        LocalDate vencimento,
        @NotNull(message = "Status é obrigatório")
        @Schema(description = "Status atual da cobranca.", example = "PENDENTE")
        StatusCobrancaEnum status,
        @NotBlank(message = "Competencia é obrigatória")
        @Schema(description = "Competencia da cobranca no formato esperado pelo negocio.", example = "2026-04")
        String competencia,
        @Size(max = 255, message = "Referencia externa deve ter no máximo 255 caracteres")
        @Schema(description = "Identificador externo da cobranca no gateway, usado para correlacionar webhooks.", example = "pay_000123456")
        String referenciaExterna,
        @NotNull(message = "Unidade é obrigatória")
        @Schema(description = "Identificador da unidade cobrada.", example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId
) {
}
