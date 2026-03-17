package com.codewithus.kondo.dto.contabancaria;

import com.codewithus.kondo.domain.enums.TipoContaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ContaBancariaRequestDTO(
        @NotBlank(message = "Banco é obrigatório")
        String banco,
        @NotBlank(message = "Agencia é obrigatória")
        String agencia,
        @NotBlank(message = "Conta é obrigatória")
        String conta,
        @NotNull(message = "Tipo é obrigatório")
        TipoContaEnum tipo,
        @NotNull(message = "Condominio é obrigatório")
        UUID condominioId
) {
}
