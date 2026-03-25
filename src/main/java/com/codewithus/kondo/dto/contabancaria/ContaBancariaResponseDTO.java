package com.codewithus.kondo.dto.contabancaria;

import com.codewithus.kondo.domain.enums.TipoContaEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ContaBancariaResponseDTO(
        @Schema(example = "ee0e8400-e29b-41d4-a716-446655440009")
        UUID id,
        @Schema(example = "Banco do Brasil")
        String banco,
        @Schema(example = "1234-5")
        String agencia,
        @Schema(example = "98765-4")
        String conta,
        @Schema(example = "CORRENTE")
        TipoContaEnum tipo,
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
