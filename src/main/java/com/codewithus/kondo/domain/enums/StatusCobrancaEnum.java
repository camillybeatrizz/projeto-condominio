package com.codewithus.kondo.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status financeiro de uma cobranca.")
public enum StatusCobrancaEnum {
    @Schema(description = "Cobranca em aberto, ainda sem pagamento.")
    ABERTA,
    @Schema(description = "Cobranca quitada.")
    PAGA,
    @Schema(description = "Cobranca vencida e ainda nao paga.")
    VENCIDA
}
