package com.codewithus.kondo.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

public record EnderecoResponseDTO(
        @Schema(example = "880e8400-e29b-41d4-a716-446655440003")
        UUID id,
        @Schema(example = "Rua das Palmeiras")
        String logradouro,
        @Schema(example = "120")
        String numero,
        @Schema(example = "Ao lado da praca central")
        String complemento,
        @Schema(example = "Centro")
        String bairro,
        @Schema(example = "Joao Pessoa")
        String cidade,
        @Schema(example = "PB")
        String estado,
        @Schema(example = "58000-000")
        String cep,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-03-18T10:30:00")
        LocalDateTime updatedAt
) {
}
