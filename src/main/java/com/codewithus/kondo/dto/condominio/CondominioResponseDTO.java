package com.codewithus.kondo.dto.condominio;

import java.time.LocalDateTime;
import java.util.UUID;

public record CondominioResponseDTO(
        UUID id,
        String nome,
        String cnpj,
        String telefone,
        UUID enderecoId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
