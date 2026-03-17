package com.codewithus.kondo;

import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioIntegrationTest extends IntegrationTestSupport {

    @Test
    void deveCriarUsuarioSemExporSenhaNaResposta() throws Exception {
        String payload = """
                {
                  "nome": "Aline Silva",
                  "email": "aline@example.com",
                  "senha": "senha-segura",
                  "telefone": "(83) 98888-7777",
                  "ativo": true
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Aline Silva"))
                .andExpect(jsonPath("$.email").value("aline@example.com"))
                .andExpect(jsonPath("$.telefone").value("(83) 98888-7777"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void deveRetornar400QuandoEmailForInvalido() throws Exception {
        String payload = """
                {
                  "nome": "Aline Silva",
                  "email": "email-invalido",
                  "senha": "senha-segura",
                  "telefone": "(83) 98888-7777",
                  "ativo": true
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("email: Email inválido"))
                .andExpect(jsonPath("$.path").value("/usuarios"));
    }

}
