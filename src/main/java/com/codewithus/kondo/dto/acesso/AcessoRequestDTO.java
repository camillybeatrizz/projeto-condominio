package com.codewithus.kondo.dto.acesso;

import com.codewithus.kondo.domain.enums.PerfilEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AcessoRequestDTO(
        @NotNull(message = "Usuario é obrigatório")
        @Schema(description = "Identificador do usuario que recebera o acesso.", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID usuarioId,
        @Schema(description = "Condominio ao qual o acesso pertence. Obrigatorio para perfis SINDICO e MORADOR.", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId,
        @Schema(description = "Unidade vinculada ao acesso. Obrigatoria para perfil MORADOR.", example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId,
        @NotNull(message = "Perfil é obrigatório")
        @Schema(description = "Perfil concedido ao usuario.", example = "SINDICO")
        PerfilEnum perfil
) {
}
