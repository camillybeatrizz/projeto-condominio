package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.AuditoriaEvento;
import com.codewithus.kondo.repository.AuditoriaEventoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.test.context.SpringBootTest(properties = {
        "kondo.security.enabled=false"
})
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
class AuditoriaControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private AuditoriaEventoRepository auditoriaEventoRepository;

    @Test
    void deveListarEventosDeAuditoria() throws Exception {
        AuditoriaEvento evento = new AuditoriaEvento();
        evento.setTipoEvento("TESTE");
        evento.setEntidade("CREDITO");
        evento.setEntidadeId("123");
        evento.setAtor("SYSTEM");
        evento.setDetalhe("Evento criado para teste");
        auditoriaEventoRepository.save(evento);

        mockMvc.perform(get("/auditoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(auditoriaEventoRepository.findAll())
                .extracting(AuditoriaEvento::getTipoEvento)
                .contains("TESTE");
    }
}
