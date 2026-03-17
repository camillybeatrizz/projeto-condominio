package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CobrancaBusinessIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

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

    private String criarUnidadeBase() throws Exception {
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
        condominio.setCnpj("11.111.111/0001-11");
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
