package com.codewithus.kondo.dto.integration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AsaasCobrancaCommand(
        UUID cobrancaId,
        UUID unidadeId,
        UUID usuarioId,
        String pagadorNome,
        String pagadorEmail,
        String pagadorTelefone,
        BigDecimal valor,
        LocalDate vencimento,
        String competencia
) {
}
