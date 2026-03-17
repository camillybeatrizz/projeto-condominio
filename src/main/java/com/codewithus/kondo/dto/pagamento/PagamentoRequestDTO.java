package com.codewithus.kondo.dto.pagamento;

import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PagamentoRequestDTO(
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01",message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Data do pagamento é obrigatória")
        LocalDate dataPagamento,

        @NotNull(message = "Forma de pagamento é obrigatória")
        FormaPagamentoEnum forma,

        @NotBlank(message = "TransactionId é obrigatório")
        @Size(max = 255, message = "TransactionId deve ter no máximo 255 caracteres")
        String transactionId,

        @NotNull(message = "Cobrança é obrigatória")
        UUID cobrancaId
) {
}
