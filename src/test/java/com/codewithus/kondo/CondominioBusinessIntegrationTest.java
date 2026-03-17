package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CondominioBusinessIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Test
    void deveRetornar409QuandoCnpjJaExistir() throws Exception {
        Endereco endereco1 = novoEndereco("100");
        Endereco endereco2 = novoEndereco("200");

        String payload1 = """
                {
                  "nome": "Condominio A",
                  "cnpj": "12.345.678/0001-90",
                  "telefone": "(83) 99999-0001",
                  "enderecoId": "%s"
                }
                """.formatted(endereco1.getId());

        String payload2 = """
                {
                  "nome": "Condominio B",
                  "cnpj": "12.345.678/0001-90",
                  "telefone": "(83) 99999-0002",
                  "enderecoId": "%s"
                }
                """.formatted(endereco2.getId());

        mockMvc.perform(post("/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Já existe condomínio com este CNPJ"))
                .andExpect(jsonPath("$.path").value("/condominios"));
    }

    private Endereco novoEndereco(String numero) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero(numero);
        endereco.setComplemento("Apto");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-000");
        return enderecoRepository.save(endereco);
    }
}

