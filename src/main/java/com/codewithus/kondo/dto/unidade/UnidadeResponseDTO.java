package com.codewithus.kondo.dto.unidade;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record UnidadeResponseDTO(
        @Schema(example = "770e8400-e29b-41d4-a716-446655440002")
        UUID id,
        @Schema(example = "203")
        String numero,
        @Schema(example = "2")
        String andar,
        @Schema(example = "APARTAMENTO")
        String tipo,
        @Schema(example = "bb0e8400-e29b-41d4-a716-446655440006")
        UUID blocoId,
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID moradorId
) {
}
