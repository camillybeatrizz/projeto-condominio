package com.codewithus.kondo.dto.areacomum;

import java.time.LocalDateTime;
import java.util.UUID;

public record AreaComumResponseDTO(
        UUID id,
        String nome,
        String descricao,
        Integer capacidade,
        UUID condominioId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
