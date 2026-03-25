package com.codewithus.kondo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "kondo.security.enabled=false",
        "kondo.security.allow-insecure-open-access=false"
})
@AutoConfigureMockMvc
class SecurityFailClosedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveNegarAcessoQuandoSegurancaEstiverDesabilitadaSemOptInInseguro() throws Exception {
        mockMvc.perform(get("/condominios"))
                .andExpect(status().isForbidden());
    }
}
