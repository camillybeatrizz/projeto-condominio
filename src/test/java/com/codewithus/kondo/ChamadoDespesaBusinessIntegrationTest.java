package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChamadoDespesaBusinessIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Test
    void deveRetornar400QuandoCriarChamadoComStatusDiferenteDeAberto() throws Exception {
        Unidade unidade = criarUnidade("201", "Condominio Chamado");

        String payload = """
                {
                  "descricao": "Falta de iluminacao no corredor",
                  "status": "CONCLUIDO",
                  "dataAbertura": "2026-03-18",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        mockMvc.perform(post("/chamados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Chamado deve ser criado com status ABERTO"));
    }

    @Test
    void deveRetornar400QuandoReabrirChamadoEmAndamentoParaAberto() throws Exception {
        Unidade unidade = criarUnidade("202", "Condominio Fluxo Chamado");

        String criarPayload = """
                {
                  "descricao": "Vazamento na pia",
                  "status": "ABERTO",
                  "dataAbertura": "2026-03-18",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        String criado = postAndReturnBody("/chamados", criarPayload);
        String chamadoId = jsonField(criado, "id");

        String andamentoPayload = """
                {
                  "descricao": "Vazamento na pia",
                  "status": "ANDAMENTO",
                  "dataAbertura": "2026-03-18",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        mockMvc.perform(put("/chamados/{id}", chamadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(andamentoPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StatusChamadoEnum.ANDAMENTO.name()));

        String reabrirPayload = """
                {
                  "descricao": "Vazamento na pia",
                  "status": "ABERTO",
                  "dataAbertura": "2026-03-18",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        mockMvc.perform(put("/chamados/{id}", chamadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reabrirPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Transição de status do chamado não é permitida"));
    }

    @Test
    void deveRetornar400AoExcluirCondominioComDespesasVinculadas() throws Exception {
        Condominio condominio = criarCondominio("107", "Condominio Despesa");

        String despesaPayload = """
                {
                  "descricao": "Manutencao do elevador",
                  "valor": 950.00,
                  "data": "2026-03-10",
                  "categoria": "MANUTENCAO",
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        mockMvc.perform(post("/despesas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(despesaPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/condominios/{id}", condominio.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir condomínio que possui despesas vinculadas"));
    }

    @Test
    void deveRetornar400AoExcluirCondominioComContaBancariaVinculada() throws Exception {
        Condominio condominio = criarCondominio("108", "Condominio Conta");

        String contaPayload = """
                {
                  "banco": "Banco do Brasil",
                  "agencia": "1234",
                  "conta": "56789-0",
                  "tipo": "CORRENTE",
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        mockMvc.perform(post("/contas-bancarias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contaPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/condominios/{id}", condominio.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir condomínio que possui contas bancárias vinculadas"));
    }

    private Unidade criarUnidade(String sufixo, String nomeCondominio) {
        Condominio condominio = criarCondominio(sufixo, nomeCondominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + sufixo);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero("10" + sufixo);
        unidade.setAndar("1");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        return unidadeRepository.save(unidade);
    }

    private Condominio criarCondominio(String sufixo, String nome) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Regra " + sufixo);
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-100");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nome);
        condominio.setCnpj("71.000.000/0001-" + sufixo);
        condominio.setTelefone("(83) 99999-1000");
        condominio.setEndereco(endereco);
        return condominioRepository.save(condominio);
    }
}
