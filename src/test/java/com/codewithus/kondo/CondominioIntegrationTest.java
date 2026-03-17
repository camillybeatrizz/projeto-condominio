package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
class CondominioIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Test
    void deveCriarCondominioComEnderecoExistente() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua das Flores");
        endereco.setNumero("100");
        endereco.setComplemento("Bloco A");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-000");
        endereco = enderecoRepository.save(endereco);

        String payload = """
                {
                  "nome": "Residencial Jardim",
                  "cnpj": "77.888.999/0001-55",
                  "telefone": "(83) 99999-0000",
                  "enderecoId": "%s"
                }
                """.formatted(endereco.getId());


        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Residencial Jardim"))
                .andExpect(jsonPath("$.cnpj").value("77.888.999/0001-55"))
                .andExpect(jsonPath("$.telefone").value("(83) 99999-0000"))
                .andExpect(jsonPath("$.enderecoId").value(endereco.getId().toString()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void deveRetornar404QuandoCondominioNaoExistir() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(get("/condominios/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Condominio não encontrado"))
                .andExpect(jsonPath("$.path").value("/condominios/" + idInexistente));
    }

}
