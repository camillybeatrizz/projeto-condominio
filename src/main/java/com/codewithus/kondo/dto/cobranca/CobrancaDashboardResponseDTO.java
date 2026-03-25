package com.codewithus.kondo.dto.cobranca;

import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CobrancaDashboardResponseDTO(
        @Schema(description = "Bloco principal de indicadores agregados do dashboard financeiro.")
        CobrancaResumoResponseDTO resumo,
        @Schema(description = "Lista reduzida de cobrancas inadimplentes para cards, tabelas resumidas ou alertas visuais no dashboard.")
        List<CobrancaResponseDTO> inadimplentesRecentes,
        @Schema(description = "Lista reduzida dos pagamentos mais recentes no escopo do usuario autenticado.")
        List<PagamentoResponseDTO> pagamentosRecentes
) {
}
