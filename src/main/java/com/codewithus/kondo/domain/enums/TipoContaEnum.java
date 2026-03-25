package com.codewithus.kondo.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo da conta bancaria do condominio.")
public enum TipoContaEnum {
    @Schema(description = "Conta corrente.")
    CORRENTE,
    @Schema(description = "Conta poupanca.")
    POUPANCA
}
