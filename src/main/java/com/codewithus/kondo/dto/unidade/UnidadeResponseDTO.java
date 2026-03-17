package com.codewithus.kondo.dto.unidade;

import java.util.UUID;

public record UnidadeResponseDTO(
        UUID id,
        String numero,
        String andar,
        String tipo,
        UUID blocoId
) {
}
