package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PagamentoIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Test
    void deveCriarPagamentoComCobrancaExistente() throws Exception {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(new BigDecimal("350.00"));
        cobranca.setVencimento(LocalDate.of(2026, 3, 20));
        cobranca.setStatus(StatusCobrancaEnum.ABERTA);
        cobranca.setCompetencia("2026-03");
        cobranca = cobrancaRepository.save(cobranca);

        String payload = """
                {
                  "valor": 350.00,
                  "dataPagamento": "2026-03-13",
                  "forma": "PIX",
                  "transactionId": "TXN-20260313-001",
                  "cobrancaId": "%s"
                }
                """.formatted(cobranca.getId());

        mockMvc.perform(post("/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.valor").value(350.00))
                .andExpect(jsonPath("$.dataPagamento").value("2026-03-13"))
                .andExpect(jsonPath("$.forma").value("PIX"))
                .andExpect(jsonPath("$.transactionId").value("TXN-20260313-001"))
                .andExpect(jsonPath("$.cobrancaId").value(cobranca.getId().toString()));
    }

    @Test
    void deveRetornar404QuandoPagamentoNaoExistir() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(get("/pagamentos/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"))
                .andExpect(jsonPath("$.path").value("/pagamentos/" + idInexistente));
    }

    @Test
    void deveAtualizarPagamentoExistente() throws Exception {
        Cobranca cobrancaOriginal = new Cobranca();
        cobrancaOriginal.setValor(new BigDecimal("150.00"));
        cobrancaOriginal.setVencimento(LocalDate.of(2026, 3, 10));
        cobrancaOriginal.setStatus(StatusCobrancaEnum.ABERTA);
        cobrancaOriginal.setCompetencia("2026-03");
        cobrancaOriginal = cobrancaRepository.save(cobrancaOriginal);

        String createPayload = """
                {
                  "valor": 150.00,
                  "dataPagamento": "2026-03-11",
                  "forma": "PIX",
                  "transactionId": "TXN-UPDATE-001",
                  "cobrancaId": "%s"
                }
                """.formatted(cobrancaOriginal.getId());

        String pagamentoResponse = postAndReturnBody("/pagamentos", createPayload);
        String pagamentoId = jsonField(pagamentoResponse, "id");

        Cobranca cobrancaNova = new Cobranca();
        cobrancaNova.setValor(new BigDecimal("175.00"));
        cobrancaNova.setVencimento(LocalDate.of(2026, 4, 10));
        cobrancaNova.setStatus(StatusCobrancaEnum.ABERTA);
        cobrancaNova.setCompetencia("2026-04");
        cobrancaNova = cobrancaRepository.save(cobrancaNova);

        String updatePayload = """
                {
                  "valor": 175.00,
                  "dataPagamento": "2026-04-12",
                  "forma": "BOLETO",
                  "transactionId": "TXN-UPDATE-002",
                  "cobrancaId": "%s"
                }
                """.formatted(cobrancaNova.getId());

        mockMvc.perform(put("/pagamentos/{id}", pagamentoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pagamentoId))
                .andExpect(jsonPath("$.valor").value(175.00))
                .andExpect(jsonPath("$.dataPagamento").value("2026-04-12"))
                .andExpect(jsonPath("$.forma").value("BOLETO"))
                .andExpect(jsonPath("$.transactionId").value("TXN-UPDATE-002"))
                .andExpect(jsonPath("$.cobrancaId").value(cobrancaNova.getId().toString()));
    }

    @Test
    void deveDeletarPagamentoExistente() throws Exception {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(new BigDecimal("220.00"));
        cobranca.setVencimento(LocalDate.of(2026, 5, 10));
        cobranca.setStatus(StatusCobrancaEnum.ABERTA);
        cobranca.setCompetencia("2026-05");
        cobranca = cobrancaRepository.save(cobranca);

        String createPayload = """
                {
                  "valor": 220.00,
                  "dataPagamento": "2026-05-08",
                  "forma": "PIX",
                  "transactionId": "TXN-DELETE-001",
                  "cobrancaId": "%s"
                }
                """.formatted(cobranca.getId());

        String pagamentoResponse = postAndReturnBody("/pagamentos", createPayload);
        String pagamentoId = jsonField(pagamentoResponse, "id");

        mockMvc.perform(delete("/pagamentos/{id}", pagamentoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/pagamentos/{id}", pagamentoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pagamento não encontrado"));
    }
}
