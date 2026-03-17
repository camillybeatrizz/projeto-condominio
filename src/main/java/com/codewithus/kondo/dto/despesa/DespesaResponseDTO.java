package com.codewithus.kondo.dto.despesa;

import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaResponseDTO(
        UUID id,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        CategoriaDespesaEnum categoria,
        UUID condominioId
) {
}
