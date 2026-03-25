package com.codewithus.kondo.dto.contexto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record MeuContextoResponseDTO(
        @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
        UUID usuarioId,
        @Schema(example = "Aline Silva")
        String nome,
        @Schema(example = "aline@email.com")
        String email,
        @Schema(example = "(83) 99999-9999")
        String telefone,
        Boolean ativo,
        List<MeuAcessoResponseDTO> acessos
) {
}
