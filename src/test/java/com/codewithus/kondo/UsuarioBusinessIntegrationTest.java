package com.codewithus.kondo;

import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsuarioBusinessIntegrationTest extends IntegrationTestSupport {

    @Test
    void seEmailJaExistir() throws Exception {
        String primeiroPayload = """
                {
                "nome": "Aline Silva",
                "email": "aline@email.com",
                "senha": "123456",
                "telefone": "(83) 99999-9999",
                "ativo": true
                }
                """;
        String segundoPayload = """
                {
                "nome": "Maria Souza",
                "email": "aline@email.com",
                "senha": "abcdef",
                "telefone": "(83) 98888-8888",
                "ativo": true
                }
                """;

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(primeiroPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(segundoPayload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Já existe usuário com este email"))
                .andExpect(jsonPath("$.path").value("/usuarios"));
    }
}
