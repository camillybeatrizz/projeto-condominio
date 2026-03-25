package com.codewithus.kondo.dto.pagamento;

import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PagamentoRequestDTO(
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01",message = "Valor deve ser maior que zero")
        @Schema(description = "Valor efetivamente pago.", example = "450.00")
        BigDecimal valor,

        @NotNull(message = "Data do pagamento é obrigatória")
        @Schema(description = "Data em que o pagamento foi realizado.", example = "2026-04-08")
        LocalDate dataPagamento,

        @NotNull(message = "Forma de pagamento é obrigatória")
        @Schema(description = "Forma utilizada para pagar a cobranca.", example = "PIX")
        FormaPagamentoEnum forma,

        @NotBlank(message = "TransactionId é obrigatório")
        @Size(max = 255, message = "TransactionId deve ter no máximo 255 caracteres")
        @Schema(description = "Identificador da transacao no meio de pagamento.", example = "PIX-20260408-000123")
        String transactionId,

        @NotNull(message = "Cobrança é obrigatória")
        @Schema(description = "Identificador da cobranca quitada.", example = "aa0e8400-e29b-41d4-a716-446655440005")
        UUID cobrancaId
) {
}
