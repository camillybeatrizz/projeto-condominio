package com.codewithus.kondo.dto.fornecedor;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record FornecedorResponseDTO(
        @Schema(example = "990e8400-e29b-41d4-a716-446655440004")
        UUID id,
        @Schema(example = "Elevadores Paraiba LTDA")
        String nome,
        @Schema(example = "98.765.432/0001-10")
        String cnpj,
        @Schema(example = "(83) 98888-7777")
        String telefone,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
