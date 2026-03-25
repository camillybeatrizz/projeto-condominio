package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.AreaComum;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.repository.AreaComumRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.test.context.SpringBootTest(properties = {
        "kondo.security.enabled=false"
})
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
class AreaComumIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private AreaComumRepository areaComumRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Test
    void deveCriarListarAtualizarEArquivarAreaComum() throws Exception {
        Condominio condominio = criarCondominio("Condominio Area Comum");

        String createPayload = """
                {
                  "nome": "Salão de Festas",
                  "descricao": "Espaco para eventos do condominio",
                  "capacidade": 60,
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        String response = postAndReturnBody("/areas-comuns", createPayload);
        UUID areaId = UUID.fromString(jsonField(response, "id"));

        mockMvc.perform(get("/areas-comuns")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String updatePayload = """
                {
                  "nome": "Espaco Gourmet",
                  "descricao": "Area reformada",
                  "capacidade": 40,
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/areas-comuns/{id}", areaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk());

        AreaComum areaAtualizada = areaComumRepository.findById(areaId).orElseThrow();
        assertThat(areaAtualizada.getNome()).isEqualTo("Espaco Gourmet");
        assertThat(areaAtualizada.getCapacidade()).isEqualTo(40);

        mockMvc.perform(delete("/areas-comuns/{id}", areaId))
                .andExpect(status().isNoContent());

        AreaComum areaArquivada = areaComumRepository.findById(areaId).orElseThrow();
        assertThat(areaArquivada.getDeletedAt()).isNotNull();
        assertThat(areaArquivada.getDeletedBy()).isEqualTo("SYSTEM");

        mockMvc.perform(get("/areas-comuns")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void deveBloquearExclusaoDeCondominioComAreaComumVinculada() throws Exception {
        Condominio condominio = criarCondominio("Condominio Protegido");
        AreaComum areaComum = new AreaComum();
        areaComum.setNome("Piscina");
        areaComum.setDescricao("Piscina principal");
        areaComum.setCapacidade(20);
        areaComum.setCondominio(condominio);
        areaComumRepository.save(areaComum);

        mockMvc.perform(delete("/condominios/{id}", condominio.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void devePermitirExcluirCondominioQuandoAreaComumEstiverArquivada() throws Exception {
        Condominio condominio = criarCondominio("Condominio Arquivavel");
        AreaComum areaComum = new AreaComum();
        areaComum.setNome("Quadra");
        areaComum.setDescricao("Quadra poliesportiva");
        areaComum.setCapacidade(30);
        areaComum.setCondominio(condominio);
        areaComum = areaComumRepository.save(areaComum);

        mockMvc.perform(delete("/areas-comuns/{id}", areaComum.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/condominios/{id}", condominio.getId()))
                .andExpect(status().isNoContent());
    }

    private Condominio criarCondominio(String nome) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua das Areas");
        endereco.setNumero("100");
        endereco.setComplemento("Bloco A");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-000");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nome);
        condominio.setCnpj("91.000.000/0001-" + Math.abs(nome.hashCode() % 100));
        condominio.setTelefone("(83) 99999-1111");
        condominio.setEndereco(endereco);
        return condominioRepository.save(condominio);
    }
}
