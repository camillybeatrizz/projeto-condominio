package com.codewithus.kondo.dto.pagamento;

import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponseDTO(
        UUID id,
        BigDecimal valor,
        LocalDate dataPagamento,
        FormaPagamentoEnum forma,
        String transactionId,
        UUID cobrancaId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
