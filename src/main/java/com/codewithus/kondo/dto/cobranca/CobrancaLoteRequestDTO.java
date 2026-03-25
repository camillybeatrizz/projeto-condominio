package com.codewithus.kondo.dto.cobranca;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CobrancaLoteRequestDTO(
        @NotNull(message = "Condominio é obrigatório")
        @Schema(description = "Identificador do condomínio para o lote de cobranças.", example = "770e8400-e29b-41d4-a716-446655440001")
        UUID condominioId,

        @NotBlank(message = "Competencia é obrigatória")
        @Size(max = 20, message = "Competencia deve ter no máximo 20 caracteres")
        @Schema(description = "Competência do lote de cobranças.", example = "2026-04")
        String competencia,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @Schema(description = "Valor de cada cobrança do lote.", example = "450.00")
        BigDecimal valor,

        @NotNull(message = "Vencimento é obrigatório")
        @Schema(description = "Data de vencimento aplicada a todas as cobranças do lote.", example = "2026-04-10")
        LocalDate vencimento
) {
}
