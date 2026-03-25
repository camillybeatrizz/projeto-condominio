package com.codewithus.kondo.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ErrorResponseDTO(
        @Schema(description = "Data e hora do erro.", example = "2026-03-18T10:30:00")
        LocalDateTime timestamp,
        @Schema(description = "Codigo HTTP retornado.", example = "403")
        int status,
        @Schema(description = "Descricao resumida do erro HTTP.", example = "Forbidden")
        String error,
        @Schema(description = "Mensagem detalhando a causa do erro.", example = "Acesso negado")
        String message,
        @Schema(description = "Endpoint onde o erro ocorreu.", example = "/blocos/550e8400-e29b-41d4-a716-446655440000")
        String path
) {
}
