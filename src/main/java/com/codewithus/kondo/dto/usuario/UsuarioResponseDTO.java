package com.codewithus.kondo.dto.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String email,
        String telefone,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
