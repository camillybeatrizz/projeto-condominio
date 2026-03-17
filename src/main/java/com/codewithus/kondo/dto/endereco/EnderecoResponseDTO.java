package com.codewithus.kondo.dto.endereco;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnderecoResponseDTO(
        UUID id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
