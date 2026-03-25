package com.codewithus.kondo.dto.cobranca;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CobrancaResumoResponseDTO(
        @Schema(description = "Quantidade total de cobrancas no escopo retornado.", example = "12")
        long totalCobrancas,
        @Schema(description = "Quantidade de cobrancas com status ABERTA.", example = "5")
        long totalAbertas,
        @Schema(description = "Quantidade de cobrancas com status PAGA.", example = "4")
        long totalPagas,
        @Schema(description = "Quantidade de cobrancas inadimplentes, considerando vencidas ou em atraso.", example = "3")
        long totalInadimplentes,
        @Schema(description = "Soma do valor de todas as cobrancas no escopo.", example = "5400.00")
        BigDecimal valorTotal,
        @Schema(description = "Soma do valor das cobrancas ainda abertas.", example = "2100.00")
        BigDecimal valorAberto,
        @Schema(description = "Soma do valor das cobrancas pagas.", example = "2500.00")
        BigDecimal valorPago,
        @Schema(description = "Soma do valor das cobrancas inadimplentes.", example = "800.00")
        BigDecimal valorInadimplente
) {
}
