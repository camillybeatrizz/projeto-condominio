package com.codewithus.kondo.dto.despesa;

import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaRequestDTO(
        @NotBlank(message = "Descricao é obrigatória")
        @Schema(description = "Descricao da despesa.", example = "Compra de materiais de limpeza")
        String descricao,
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        @Schema(description = "Valor pago ou previsto para a despesa.", example = "320.50")
        BigDecimal valor,
        @NotNull(message = "Data é obrigatória")
        @Schema(description = "Data da despesa.", example = "2026-03-15")
        LocalDate data,
        @NotNull(message = "Categoria é obrigatória")
        @Schema(description = "Categoria da despesa.", example = "MANUTENCAO")
        CategoriaDespesaEnum categoria,
        @NotNull(message = "Condominio é obrigatório")
        @Schema(description = "Identificador do condominio relacionado.", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
