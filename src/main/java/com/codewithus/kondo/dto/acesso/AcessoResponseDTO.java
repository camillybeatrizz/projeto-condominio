package com.codewithus.kondo.dto.acesso;

import com.codewithus.kondo.domain.enums.PerfilEnum;

import java.util.UUID;

public record AcessoResponseDTO(
        UUID id,
        UUID usuarioId,
        PerfilEnum perfil
) {
}
