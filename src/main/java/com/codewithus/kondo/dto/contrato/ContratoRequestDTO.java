package com.codewithus.kondo.dto.contrato;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContratoRequestDTO(
        @NotBlank(message = "Descricao é obrigatória")
        @Schema(description = "Descricao resumida do contrato.", example = "Contrato de manutencao dos elevadores")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "Valor deve ser maior que zero")
        @Schema(description = "Valor mensal ou total previsto em contrato.", example = "1800.00")
        BigDecimal valor,

        @NotNull(message = "Data de inicio é obrigatória")
        @Schema(description = "Data inicial de vigencia do contrato.", example = "2026-01-01")
        LocalDate dataInicio,

        @Schema(description = "Data final de vigencia do contrato, quando houver.", example = "2026-12-31")
        LocalDate dataFim,

        @NotNull(message = "Fornecedor é obrigatório")
        @Schema(description = "Identificador do fornecedor contratado.", example = "990e8400-e29b-41d4-a716-446655440004")
        UUID fornecedorId,

        @NotNull(message = "Condominio é obrigatório")
        @Schema(description = "Identificador do condominio vinculado ao contrato.", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
