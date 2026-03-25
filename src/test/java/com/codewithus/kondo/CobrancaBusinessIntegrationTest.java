package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CobrancaBusinessIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Test
    void deveRetornar400QuandoTentarCriarCobrancaComStatusPaga() throws Exception {
        String unidadeId = criarUnidadeBase();

        String payload = """
                {
                  "valor": 500.00,
                  "vencimento": "2026-03-30",
                  "status": "PAGA",
                  "competencia": "2026-03",
                  "unidadeId": "%s"
                }
                """.formatted(unidadeId);

        mockMvc.perform(post("/cobrancas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Não é permitido criar ou atualizar cobrança diretamente com status PAGA"))
                .andExpect(jsonPath("$.path").value("/cobrancas"));
    }

    @Test
    void deveGerarReferenciaExternaAutomaticamenteAoCriarCobranca() throws Exception {
        String unidadeId = criarUnidadeBase();

        String payload = """
                {
                  "valor": 500.00,
                  "vencimento": "2026-03-30",
                  "status": "ABERTA",
                  "competencia": "2026-03",
                  "unidadeId": "%s"
                }
                """.formatted(unidadeId);

        String response = postAndReturnBody("/cobrancas", payload);
        String cobrancaId = jsonField(response, "id");

        Cobranca cobranca = cobrancaRepository.findById(java.util.UUID.fromString(cobrancaId)).orElseThrow();
        org.junit.jupiter.api.Assertions.assertNotNull(cobranca.getReferenciaExterna());
        org.junit.jupiter.api.Assertions.assertTrue(cobranca.getReferenciaExterna().startsWith("asaas-sim-"));
        org.junit.jupiter.api.Assertions.assertNotNull(cobranca.getUrlPagamentoExterno());
        org.junit.jupiter.api.Assertions.assertTrue(cobranca.getUrlPagamentoExterno().contains(cobranca.getReferenciaExterna()));
        org.junit.jupiter.api.Assertions.assertNotNull(cobranca.getPixCopiaCola());
        org.junit.jupiter.api.Assertions.assertNotNull(cobranca.getPixQrCodeBase64());
        org.junit.jupiter.api.Assertions.assertNotNull(cobranca.getPixExpiracao());
    }

    @Test
    void deveRetornarDetalhesPixDaCobranca() throws Exception {
        String unidadeId = criarUnidadeBase();

        String payload = """
                {
                  "valor": 500.00,
                  "vencimento": "2026-03-30",
                  "status": "ABERTA",
                  "competencia": "2026-03",
                  "unidadeId": "%s"
                }
                """.formatted(unidadeId);

        String response = postAndReturnBody("/cobrancas", payload);
        String cobrancaId = jsonField(response, "id");

        mockMvc.perform(get("/cobrancas/" + cobrancaId + "/pix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cobrancaId").value(cobrancaId))
                .andExpect(jsonPath("$.referenciaExterna").value(org.hamcrest.Matchers.startsWith("asaas-sim-")))
                .andExpect(jsonPath("$.urlPagamentoExterno").isNotEmpty())
                .andExpect(jsonPath("$.pixCopiaCola").isNotEmpty())
                .andExpect(jsonPath("$.pixQrCodeBase64").isNotEmpty())
                .andExpect(jsonPath("$.pixExpiracao").isNotEmpty());
    }

    @Test
    void deveManterReferenciaExternaGlobalmenteUnicaMesmoAposExclusaoLogica() throws Exception {
        String unidadeId = criarUnidadeBase();

        String primeira = postAndReturnBody("/cobrancas", """
                {
                  "valor": 450.00,
                  "vencimento": "2026-06-20",
                  "status": "ABERTA",
                  "competencia": "2026-06",
                  "referenciaExterna": "ref-global-001",
                  "unidadeId": "%s"
                }
                """.formatted(unidadeId));

        String cobrancaId = jsonField(primeira, "id");

        mockMvc.perform(delete("/cobrancas/{id}", cobrancaId))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/cobrancas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "valor": 500.00,
                                  "vencimento": "2026-07-20",
                                  "status": "ABERTA",
                                  "competencia": "2026-07",
                                  "referenciaExterna": "ref-global-001",
                                  "unidadeId": "%s"
                                }
                                """.formatted(unidadeId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe cobrança com esta referência externa"));
    }

    @Test
    void deveBloquearDuplicidadeDeCompetenciaEntreCobrancasAtivasDaMesmaUnidade() throws Exception {
        String unidadeId = criarUnidadeBase();

        postAndReturnBody("/cobrancas", """
                {
                  "valor": 450.00,
                  "vencimento": "2026-08-20",
                  "status": "ABERTA",
                  "competencia": "2026-08",
                  "unidadeId": "%s"
                }
                """.formatted(unidadeId));

        mockMvc.perform(post("/cobrancas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "valor": 500.00,
                                  "vencimento": "2026-08-25",
                                  "status": "ABERTA",
                                  "competencia": "2026-08",
                                  "unidadeId": "%s"
                                }
                                """.formatted(unidadeId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe cobrança ativa com esta competência para a unidade informada"));
    }

    private String criarUnidadeBase() throws Exception {
        String sufixo = java.util.UUID.randomUUID().toString().substring(0, 8);

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Base");
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-100");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome("Condominio Base");
        condominio.setCnpj("11.111.111/" + sufixo);
        condominio.setTelefone("(83) 99999-1000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco A");
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        String unidadePayload = """
                {
                  "numero": "101",
                  "andar": "1",
                  "tipo": "Apartamento",
                  "blocoId": "%s"
                }
                """.formatted(bloco.getId());

        String unidadeResponse = postAndReturnBody("/unidades", unidadePayload);
        return jsonField(unidadeResponse, "id");
    }
}
