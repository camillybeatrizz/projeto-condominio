package com.codewithus.kondo.dto.contrato;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContratoResponseDTO(
        UUID id,
        String descricao,
        BigDecimal valor,
        LocalDate dataInicio,
        LocalDate dataFim,
        UUID fornecedorId,
        UUID condominioId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
