package com.codewithus.kondo.dto.bloco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BlocoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome ou identificacao do bloco.", example = "Bloco A")
        String nome,
        @NotNull(message = "Condominio é obrigatório")
        @Schema(description = "Identificador do condominio ao qual o bloco pertence.", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
