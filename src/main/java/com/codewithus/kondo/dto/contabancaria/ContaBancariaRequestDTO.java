package com.codewithus.kondo.dto.contabancaria;

import com.codewithus.kondo.domain.enums.TipoContaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ContaBancariaRequestDTO(
        @NotBlank(message = "Banco é obrigatório")
        @Schema(description = "Nome do banco.", example = "Banco do Brasil")
        String banco,
        @NotBlank(message = "Agencia é obrigatória")
        @Schema(description = "Numero da agencia.", example = "1234-5")
        String agencia,
        @NotBlank(message = "Conta é obrigatória")
        @Schema(description = "Numero da conta.", example = "98765-4")
        String conta,
        @NotNull(message = "Tipo é obrigatório")
        @Schema(description = "Tipo da conta bancaria.", example = "CORRENTE")
        TipoContaEnum tipo,
        @NotNull(message = "Condominio é obrigatório")
        @Schema(description = "Identificador do condominio dono da conta.", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
