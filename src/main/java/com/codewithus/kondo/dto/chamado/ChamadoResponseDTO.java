package com.codewithus.kondo.dto.chamado;

import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record ChamadoResponseDTO(
        @Schema(example = "dd0e8400-e29b-41d4-a716-446655440008")
        UUID id,
        @Schema(example = "Vazamento identificado na area de servico da unidade.")
        String descricao,
        @Schema(example = "ABERTO")
        StatusChamadoEnum status,
        @Schema(example = "2026-03-18")
        LocalDate dataAbertura,
        @Schema(example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId
) {
}
