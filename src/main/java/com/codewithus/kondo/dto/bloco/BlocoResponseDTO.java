package com.codewithus.kondo.dto.bloco;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record BlocoResponseDTO(
        @Schema(example = "bb0e8400-e29b-41d4-a716-446655440006")
        UUID id,
        @Schema(example = "Bloco A")
        String nome,
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
