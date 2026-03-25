package com.codewithus.kondo.dto.cobranca;

import java.util.List;

public record CobrancaLoteResponseDTO(
        int totalUnidades,
        int cobrancasCriadas,
        int cobrancasIgnoradas,
        List<CobrancaResponseDTO> cobrancas
) {
}
