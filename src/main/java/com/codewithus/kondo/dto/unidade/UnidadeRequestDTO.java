package com.codewithus.kondo.dto.unidade;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UnidadeRequestDTO(
        @NotBlank(message = "Numero é obrigatório")
        @Schema(description = "Numero identificador da unidade.", example = "203")
        String numero,
        @NotBlank(message = "Andar é obrigatório")
        @Schema(description = "Andar onde a unidade esta localizada.", example = "2")
        String andar,
        @NotBlank(message = "Tipo é obrigatório")
        @Schema(description = "Tipo da unidade.", example = "APARTAMENTO")
        String tipo,
        @NotNull(message = "Bloco é obrigatório")
        @Schema(description = "Identificador do bloco ao qual a unidade pertence.", example = "bb0e8400-e29b-41d4-a716-446655440006")
        UUID blocoId,
        @Schema(description = "Identificador do morador vinculado a unidade, quando houver.", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID moradorId
) {
}
