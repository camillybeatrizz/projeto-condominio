package com.codewithus.kondo.dto.acesso;

import com.codewithus.kondo.domain.enums.PerfilEnum;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AcessoRequestDTO(
        @NotNull(message = "Usuario é obrigatório")
        UUID usuarioId,
        @NotNull(message = "Perfil é obrigatório")
        PerfilEnum perfil
) {
}
