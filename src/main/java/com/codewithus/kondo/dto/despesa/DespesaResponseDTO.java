package com.codewithus.kondo.dto.despesa;

import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaResponseDTO(
        @Schema(example = "110e8400-e29b-41d4-a716-446655440011")
        UUID id,
        @Schema(example = "Compra de materiais de limpeza")
        String descricao,
        @Schema(example = "320.50")
        BigDecimal valor,
        @Schema(example = "2026-03-15")
        LocalDate data,
        @Schema(example = "MANUTENCAO")
        CategoriaDespesaEnum categoria,
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
