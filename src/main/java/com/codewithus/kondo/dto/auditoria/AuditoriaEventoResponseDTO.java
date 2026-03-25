package com.codewithus.kondo.dto.auditoria;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditoriaEventoResponseDTO(
        UUID id,
        String tipoEvento,
        String entidade,
        String entidadeId,
        String ator,
        String detalhe,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
