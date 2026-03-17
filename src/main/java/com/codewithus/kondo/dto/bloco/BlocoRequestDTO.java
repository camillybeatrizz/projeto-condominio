package com.codewithus.kondo.dto.bloco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BlocoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotNull(message = "Condominio é obrigatório")
        UUID condominioId
) {
}
