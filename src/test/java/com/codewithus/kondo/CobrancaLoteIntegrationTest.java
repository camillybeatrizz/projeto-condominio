package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CobrancaLoteIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Test
    void deveGerarCobrancasEmLoteSomenteParaUnidadesSemCompetenciaExistente() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominioComDuasUnidades("33.333.333/0001-33");
        List<Unidade> unidades = unidadeRepository.findAllByBloco_Condominio_Id(condominio.getId());
        Unidade unidadeComCobranca = unidades.get(0);

        Cobranca existente = new Cobranca();
        existente.setValor(new BigDecimal("450.00"));
        existente.setVencimento(LocalDate.of(2026, 4, 10));
        existente.setStatus(StatusCobrancaEnum.ABERTA);
        existente.setCompetencia("2026-04");
        existente.setUnidade(unidadeComCobranca);
        cobrancaRepository.save(existente);

        String payload = """
                {
                  "condominioId": "%s",
                  "competencia": "2026-04",
                  "valor": 500.00,
                  "vencimento": "2026-04-10"
                }
                """.formatted(condominio.getId());

        mockMvc.perform(post("/cobrancas/gerar-lote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalUnidades").value(2))
                .andExpect(jsonPath("$.cobrancasCriadas").value(1))
                .andExpect(jsonPath("$.cobrancasIgnoradas").value(1))
                .andExpect(jsonPath("$.cobrancas", hasSize(1)));

        List<Cobranca> cobrancas = cobrancaRepository.findAll();
        org.junit.jupiter.api.Assertions.assertEquals(2, cobrancas.size());
    }

    @Test
    void deveListarInadimplentesComEndpointDedicado() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominioComDuasUnidades("44.444.444/0001-44");
        List<Unidade> unidades = unidadeRepository.findAllByBloco_Condominio_Id(condominio.getId());

        Cobranca vencida = new Cobranca();
        vencida.setValor(new BigDecimal("400.00"));
        vencida.setVencimento(LocalDate.of(2026, 3, 10));
        vencida.setStatus(StatusCobrancaEnum.ABERTA);
        vencida.setCompetencia("2026-03");
        vencida.setUnidade(unidades.get(0));
        cobrancaRepository.save(vencida);

        Cobranca paga = new Cobranca();
        paga.setValor(new BigDecimal("400.00"));
        paga.setVencimento(LocalDate.of(2026, 3, 10));
        paga.setStatus(StatusCobrancaEnum.PAGA);
        paga.setCompetencia("2026-03");
        paga.setUnidade(unidades.get(1));
        cobrancaRepository.save(paga);

        mockMvc.perform(get("/cobrancas/inadimplentes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].competencia").value("2026-03"))
                .andExpect(jsonPath("$.content[0].status").value("ABERTA"));
    }

    @Test
    void devePermitirReutilizarCompetenciaDaCobrancaAposExclusaoLogica() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominioComDuasUnidades("55.555.555/0001-55");
        Unidade unidade = unidadeRepository.findAllByBloco_Condominio_Id(condominio.getId()).get(0);

        String payload = """
                {
                  "valor": 450.00,
                  "vencimento": "2026-06-20",
                  "status": "ABERTA",
                  "competencia": "2026-06",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        String response = postAndReturnBody("/cobrancas", payload);
        String cobrancaId = jsonField(response, "id");

        mockMvc.perform(delete("/cobrancas/{id}", cobrancaId))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/cobrancas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.competencia").value("2026-06"));

        Cobranca arquivada = cobrancaRepository.findById(java.util.UUID.fromString(cobrancaId)).orElseThrow();
        assertThat(arquivada.getDeletedAt()).isNotNull();
    }

    private Condominio criarCondominioComDuasUnidades(String cnpj) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Lote");
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-100");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome("Condominio Lote");
        condominio.setCnpj(cnpj);
        condominio.setTelefone("(83) 99999-3333");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco A");
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade1 = new Unidade();
        unidade1.setNumero("101");
        unidade1.setAndar("1");
        unidade1.setTipo("Apartamento");
        unidade1.setBloco(bloco);
        unidadeRepository.save(unidade1);

        Unidade unidade2 = new Unidade();
        unidade2.setNumero("102");
        unidade2.setAndar("1");
        unidade2.setTipo("Apartamento");
        unidade2.setBloco(bloco);
        unidadeRepository.save(unidade2);

        return condominio;
    }
}
