package com.codewithus.kondo.dto.condominio;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record CondominioResponseDTO(
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID id,
        @Schema(example = "Residencial Bosque das Flores")
        String nome,
        @Schema(example = "12.345.678/0001-99")
        String cnpj,
        @Schema(example = "(83) 3333-4444")
        String telefone,
        @Schema(example = "880e8400-e29b-41d4-a716-446655440003")
        UUID enderecoId,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
