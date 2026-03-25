package com.codewithus.kondo.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(example = "Maria da Silva")
        String nome,
        @Schema(example = "maria.silva@kondo.com")
        String email,
        @Schema(example = "9f41b765-2d1f-4d59-9f65-6f152c9f7c0d")
        String externalId,
        @Schema(example = "(83) 99999-0000")
        String telefone,
        @Schema(example = "true")
        Boolean ativo,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
