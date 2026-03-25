package com.codewithus.kondo.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Forma usada para registrar o pagamento de uma cobranca.")
public enum FormaPagamentoEnum {
    @Schema(description = "Pagamento realizado via PIX.")
    PIX,
    @Schema(description = "Pagamento realizado por boleto bancario.")
    BOLETO
}
