package com.codewithus.kondo.dto.acesso;

import com.codewithus.kondo.domain.enums.PerfilEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AcessoResponseDTO(
        @Schema(example = "cc0e8400-e29b-41d4-a716-446655440007")
        UUID id,
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID usuarioId,
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId,
        @Schema(example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId,
        @Schema(example = "SINDICO")
        PerfilEnum perfil
) {
}
