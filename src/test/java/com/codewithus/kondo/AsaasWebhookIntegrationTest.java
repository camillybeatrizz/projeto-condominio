package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.repository.AuditoriaEventoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Instant;
import java.util.HexFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.test.context.SpringBootTest(properties = {
        "kondo.security.enabled=false",
        "kondo.integrations.asaas.webhook.hmac-secret=asaas-hmac-test-secret",
        "kondo.integrations.asaas.webhook.allowed-timestamp-skew-seconds=300"
})
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
class AsaasWebhookIntegrationTest extends IntegrationTestSupport {

    private static final String HMAC_SECRET = "asaas-hmac-test-secret";

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private AuditoriaEventoRepository auditoriaEventoRepository;

    @Test
    void deveProcessarWebhookDePagamentoRecebido() throws Exception {
        Cobranca cobranca = criarCobrancaAberta("asaas-pay-001");

        String payload = """
                {
                  "id": "evt_001",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "asaas-pay-001",
                    "value": 350.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        performSignedWebhook(payload)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSED"));

        Cobranca cobrancaAtualizada = cobrancaRepository.findById(cobranca.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals(StatusCobrancaEnum.PAGA, cobrancaAtualizada.getStatus());
        org.junit.jupiter.api.Assertions.assertTrue(pagamentoRepository.findByTransactionId("asaas-pay-001").isPresent());
        org.junit.jupiter.api.Assertions.assertTrue(
                auditoriaEventoRepository.findAll().stream()
                        .anyMatch(evento -> "PAGAMENTO_PROCESSADO_POR_WEBHOOK".equals(evento.getTipoEvento()))
        );
        org.junit.jupiter.api.Assertions.assertTrue(
                auditoriaEventoRepository.findAll().stream()
                        .anyMatch(evento -> "COBRANCA_MARCADA_PAGA".equals(evento.getTipoEvento()))
        );
    }

    @Test
    void deveIgnorarEventoDuplicado() throws Exception {
        criarCobrancaAberta("asaas-pay-002");

        String payload = """
                {
                  "id": "evt_duplicado",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "asaas-pay-002",
                    "value": 410.00,
                    "billingType": "BOLETO",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        performSignedWebhook(payload)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSED"));

        performSignedWebhook(payload)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IGNORED"))
                .andExpect(jsonPath("$.message").value("Evento ja processado anteriormente"));
    }

    @Test
    void deveLocalizarCobrancaPeloPaymentLinkQuandoIdNaoBater() throws Exception {
        criarCobrancaAberta("plink_001");

        String payload = """
                {
                  "id": "evt_payment_link",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "asaas-pay-real-009",
                    "paymentLink": "plink_001",
                    "value": 410.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        performSignedWebhook(payload)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSED"));

        org.junit.jupiter.api.Assertions.assertTrue(pagamentoRepository.findByTransactionId("asaas-pay-real-009").isPresent());
    }

    @Test
    void deveRejeitarWebhookComAssinaturaInvalida() throws Exception {
        String payload = """
                {
                  "id": "evt_assinatura_invalida",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "asaas-pay-003",
                    "value": 200.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        mockMvc.perform(post("/webhooks/asaas")
                        .header("asaas-timestamp", Instant.now().getEpochSecond())
                        .header("asaas-signature", "sha256=deadbeef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Acesso negado"));
    }

    @Test
    void deveRejeitarWebhookSemAssinatura() throws Exception {
        String payload = """
                {
                  "id": "evt_sem_assinatura",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "asaas-pay-005",
                    "value": 200.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        mockMvc.perform(post("/webhooks/asaas")
                        .header("asaas-timestamp", Instant.now().getEpochSecond())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Acesso negado"));
    }

    @Test
    void deveRejeitarWebhookComTimestampExpirado() throws Exception {
        String payload = """
                {
                  "id": "evt_expirado",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "asaas-pay-006",
                    "value": 200.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        long expiredTimestamp = Instant.now().minusSeconds(301).getEpochSecond();

        mockMvc.perform(post("/webhooks/asaas")
                        .header("asaas-timestamp", expiredTimestamp)
                        .header("asaas-signature", sign(payload, expiredTimestamp))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Acesso negado"));
    }

    @Test
    void deveIgnorarEventoSemAcaoConfigurada() throws Exception {
        String payload = """
                {
                  "id": "evt_outro",
                  "event": "PAYMENT_CREATED",
                  "payment": {
                    "id": "asaas-pay-004",
                    "value": 200.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        performSignedWebhook(payload)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IGNORED"));
    }

    private Cobranca criarCobrancaAberta(String referenciaExterna) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(new BigDecimal("350.00"));
        cobranca.setVencimento(LocalDate.of(2026, 3, 30));
        cobranca.setStatus(StatusCobrancaEnum.ABERTA);
        cobranca.setCompetencia("2026-03");
        cobranca.setReferenciaExterna(referenciaExterna);
        return cobrancaRepository.save(cobranca);
    }

    private ResultActions performSignedWebhook(String payload) throws Exception {
        long timestamp = Instant.now().getEpochSecond();
        return mockMvc.perform(post("/webhooks/asaas")
                .header("asaas-timestamp", timestamp)
                .header("asaas-signature", sign(payload, timestamp))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload));
    }

    private String sign(String payload, long timestamp) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(HMAC_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signature = mac.doFinal((timestamp + "." + payload).getBytes(StandardCharsets.UTF_8));
        return "sha256=" + HexFormat.of().formatHex(signature);
    }
}
