package com.codewithus.kondo.dto.contexto;

import com.codewithus.kondo.domain.enums.PerfilEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record MeuAcessoResponseDTO(
        @Schema(example = "cc0e8400-e29b-41d4-a716-446655440007")
        UUID acessoId,
        @Schema(example = "SINDICO")
        PerfilEnum perfil,
        @Schema(example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId,
        @Schema(example = "Condominio Central")
        String condominioNome,
        @Schema(example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId,
        @Schema(example = "101")
        String unidadeNumero
) {
}
