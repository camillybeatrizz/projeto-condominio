package com.codewithus.kondo.dto.unidade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UnidadeRequestDTO(
        @NotBlank(message = "Numero é obrigatório")
        String numero,
        @NotBlank(message = "Andar é obrigatório")
        String andar,
        @NotBlank(message = "Tipo é obrigatório")
        String tipo,
        @NotNull(message = "Bloco é obrigatório")
        UUID blocoId
) {
}
