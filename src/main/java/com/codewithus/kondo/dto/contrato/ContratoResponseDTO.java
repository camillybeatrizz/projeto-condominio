package com.codewithus.kondo.dto.contrato;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContratoResponseDTO(
        @Schema(example = "ff0e8400-e29b-41d4-a716-446655440010")
        UUID id,
        @Schema(example = "Contrato de manutencao dos elevadores")
        String descricao,
        @Schema(example = "1800.00")
        BigDecimal valor,
        @Schema(example = "2026-01-01")
        LocalDate dataInicio,
        @Schema(example = "2026-12-31")
        LocalDate dataFim,
        @Schema(example = "990e8400-e29b-41d4-a716-446655440004")
        UUID fornecedorId,
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
