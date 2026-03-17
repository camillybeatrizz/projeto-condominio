package com.codewithus.kondo.dto.bloco;

import java.util.UUID;

public record BlocoResponseDTO(
        UUID id,
        String nome,
        UUID condominioId
) {
}
