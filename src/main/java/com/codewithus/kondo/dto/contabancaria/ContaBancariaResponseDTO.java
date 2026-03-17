package com.codewithus.kondo.dto.contabancaria;

import com.codewithus.kondo.domain.enums.TipoContaEnum;

import java.util.UUID;

public record ContaBancariaResponseDTO(
        UUID id,
        String banco,
        String agencia,
        String conta,
        TipoContaEnum tipo,
        UUID condominioId
) {
}
