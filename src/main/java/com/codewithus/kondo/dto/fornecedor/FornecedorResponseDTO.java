package com.codewithus.kondo.dto.fornecedor;

import java.time.LocalDateTime;
import java.util.UUID;

public record FornecedorResponseDTO(
        UUID id,
        String nome,
        String cnpj,
        String telefone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
