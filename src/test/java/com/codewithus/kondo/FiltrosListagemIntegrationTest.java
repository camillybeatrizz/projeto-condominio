package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Chamado;
import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.ContaBancaria;
import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Contrato;
import com.codewithus.kondo.domain.entity.Despesa;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.domain.entity.Pagamento;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;
import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.domain.enums.TipoContaEnum;
import com.codewithus.kondo.repository.ChamadoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.DespesaRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FiltrosListagemIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Test
    void deveFiltrarChamadosPorStatus() throws Exception {
        limparBaseDeTestes();

        Chamado aberto = new Chamado();
        aberto.setDescricao("Porta com problema");
        aberto.setStatus(StatusChamadoEnum.ABERTO);
        aberto.setDataAbertura(LocalDate.of(2026, 3, 10));
        chamadoRepository.save(aberto);

        Chamado concluido = new Chamado();
        concluido.setDescricao("Interfone ajustado");
        concluido.setStatus(StatusChamadoEnum.CONCLUIDO);
        concluido.setDataAbertura(LocalDate.of(2026, 3, 11));
        chamadoRepository.save(concluido);

        mockMvc.perform(get("/chamados")
                        .param("status", "ABERTO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao").value("Porta com problema"))
                .andExpect(jsonPath("$.content[0].status").value("ABERTO"));
    }

    @Test
    void deveFiltrarCobrancasPorStatusECompetencia() throws Exception {
        limparBaseDeTestes();

        Cobranca aberta = new Cobranca();
        aberta.setValor(new BigDecimal("350.00"));
        aberta.setVencimento(LocalDate.of(2026, 3, 20));
        aberta.setStatus(StatusCobrancaEnum.ABERTA);
        aberta.setCompetencia("2026-03");
        cobrancaRepository.save(aberta);

        Cobranca vencida = new Cobranca();
        vencida.setValor(new BigDecimal("420.00"));
        vencida.setVencimento(LocalDate.of(2026, 4, 20));
        vencida.setStatus(StatusCobrancaEnum.VENCIDA);
        vencida.setCompetencia("2026-04");
        cobrancaRepository.save(vencida);

        mockMvc.perform(get("/cobrancas")
                        .param("status", "ABERTA")
                        .param("competencia", "2026-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status").value("ABERTA"))
                .andExpect(jsonPath("$.content[0].competencia").value("2026-03"));
    }

    @Test
    void deveRetornarResumoDeCobrancas() throws Exception {
        limparBaseDeTestes();

        Cobranca aberta = new Cobranca();
        aberta.setValor(new BigDecimal("350.00"));
        aberta.setVencimento(LocalDate.now().plusDays(5));
        aberta.setStatus(StatusCobrancaEnum.ABERTA);
        aberta.setCompetencia("2026-03");
        cobrancaRepository.save(aberta);

        Cobranca paga = new Cobranca();
        paga.setValor(new BigDecimal("420.00"));
        paga.setVencimento(LocalDate.now().minusDays(2));
        paga.setStatus(StatusCobrancaEnum.PAGA);
        paga.setCompetencia("2026-03");
        cobrancaRepository.save(paga);

        Cobranca vencida = new Cobranca();
        vencida.setValor(new BigDecimal("180.00"));
        vencida.setVencimento(LocalDate.now().minusDays(3));
        vencida.setStatus(StatusCobrancaEnum.VENCIDA);
        vencida.setCompetencia("2026-02");
        cobrancaRepository.save(vencida);

        mockMvc.perform(get("/cobrancas/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCobrancas").value(3))
                .andExpect(jsonPath("$.totalAbertas").value(1))
                .andExpect(jsonPath("$.totalPagas").value(1))
                .andExpect(jsonPath("$.totalInadimplentes").value(1))
                .andExpect(jsonPath("$.valorTotal").value(950.00))
                .andExpect(jsonPath("$.valorAberto").value(350.00))
                .andExpect(jsonPath("$.valorPago").value(420.00))
                .andExpect(jsonPath("$.valorInadimplente").value(180.00));
    }

    @Test
    void deveRetornarDashboardFinanceiroDeCobrancas() throws Exception {
        limparBaseDeTestes();

        Cobranca aberta = new Cobranca();
        aberta.setValor(new BigDecimal("350.00"));
        aberta.setVencimento(LocalDate.now().plusDays(5));
        aberta.setStatus(StatusCobrancaEnum.ABERTA);
        aberta.setCompetencia("2026-03");
        aberta = cobrancaRepository.save(aberta);

        Cobranca paga = new Cobranca();
        paga.setValor(new BigDecimal("420.00"));
        paga.setVencimento(LocalDate.now().minusDays(2));
        paga.setStatus(StatusCobrancaEnum.PAGA);
        paga.setCompetencia("2026-03");
        paga = cobrancaRepository.save(paga);

        Cobranca vencida = new Cobranca();
        vencida.setValor(new BigDecimal("180.00"));
        vencida.setVencimento(LocalDate.now().minusDays(3));
        vencida.setStatus(StatusCobrancaEnum.VENCIDA);
        vencida.setCompetencia("2026-02");
        vencida = cobrancaRepository.save(vencida);

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(new BigDecimal("420.00"));
        pagamento.setDataPagamento(LocalDate.now());
        pagamento.setForma(FormaPagamentoEnum.PIX);
        pagamento.setTransactionId("TX-DASH-" + UUID.randomUUID());
        pagamento.setCobranca(paga);
        pagamentoRepository.save(pagamento);

        mockMvc.perform(get("/cobrancas/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resumo.totalCobrancas").value(3))
                .andExpect(jsonPath("$.resumo.totalInadimplentes").value(1))
                .andExpect(jsonPath("$.inadimplentesRecentes", hasSize(1)))
                .andExpect(jsonPath("$.inadimplentesRecentes[0].id").value(vencida.getId().toString()))
                .andExpect(jsonPath("$.pagamentosRecentes", hasSize(1)))
                .andExpect(jsonPath("$.pagamentosRecentes[0].cobrancaId").value(paga.getId().toString()));
    }

    @Test
    void deveFiltrarPagamentosPorIntervaloDeDatas() throws Exception {
        limparBaseDeTestes();

        Cobranca cobrancaMarco = criarCobranca("2026-03", "300.00");
        Cobranca cobrancaAbril = criarCobranca("2026-04", "450.00");

        Pagamento pagamentoMarco = new Pagamento();
        pagamentoMarco.setValor(new BigDecimal("300.00"));
        pagamentoMarco.setDataPagamento(LocalDate.of(2026, 3, 15));
        pagamentoMarco.setForma(FormaPagamentoEnum.PIX);
        pagamentoMarco.setTransactionId("TX-" + UUID.randomUUID());
        pagamentoMarco.setCobranca(cobrancaMarco);
        pagamentoRepository.save(pagamentoMarco);

        Pagamento pagamentoAbril = new Pagamento();
        pagamentoAbril.setValor(new BigDecimal("450.00"));
        pagamentoAbril.setDataPagamento(LocalDate.of(2026, 4, 18));
        pagamentoAbril.setForma(FormaPagamentoEnum.BOLETO);
        pagamentoAbril.setTransactionId("TX-" + UUID.randomUUID());
        pagamentoAbril.setCobranca(cobrancaAbril);
        pagamentoRepository.save(pagamentoAbril);

        mockMvc.perform(get("/pagamentos")
                        .param("dataInicio", "2026-03-01")
                        .param("dataFim", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].dataPagamento").value("2026-03-15"))
                .andExpect(jsonPath("$.content[0].cobrancaId").value(cobrancaMarco.getId().toString()));
    }

    @Test
    void deveRetornar400QuandoIntervaloDeDatasDePagamentoForInvalido() throws Exception {
        limparBaseDeTestes();

        mockMvc.perform(get("/pagamentos")
                        .param("dataInicio", "2026-04-01")
                        .param("dataFim", "2026-03-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Data inicial não pode ser maior que a data final"));
    }

    @Test
    void deveFiltrarDespesasPorCategoriaEIntervaloDeData() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominio("Condominio Despesas");

        Despesa manutencao = new Despesa();
        manutencao.setDescricao("Conserto do portao");
        manutencao.setValor(new BigDecimal("900.00"));
        manutencao.setData(LocalDate.of(2026, 5, 10));
        manutencao.setCategoria(CategoriaDespesaEnum.MANUTENCAO);
        manutencao.setCondominio(condominio);
        despesaRepository.save(manutencao);

        Despesa limpeza = new Despesa();
        limpeza.setDescricao("Produtos de limpeza");
        limpeza.setValor(new BigDecimal("250.00"));
        limpeza.setData(LocalDate.of(2026, 6, 10));
        limpeza.setCategoria(CategoriaDespesaEnum.LIMPEZA);
        limpeza.setCondominio(condominio);
        despesaRepository.save(limpeza);

        mockMvc.perform(get("/despesas")
                        .param("categoria", "MANUTENCAO")
                        .param("dataInicio", "2026-05-01")
                        .param("dataFim", "2026-05-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao").value("Conserto do portao"))
                .andExpect(jsonPath("$.content[0].categoria").value("MANUTENCAO"));
    }

    @Test
    void deveRetornar400QuandoIntervaloDeDatasDeDespesaForInvalido() throws Exception {
        limparBaseDeTestes();

        mockMvc.perform(get("/despesas")
                        .param("dataInicio", "2026-06-01")
                        .param("dataFim", "2026-05-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Data inicial não pode ser maior que a data final"));
    }

    @Test
    void deveFiltrarContratosPorFornecedorEIntervaloDeVigencia() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominio("Condominio Contratos");
        Fornecedor fornecedorA = criarFornecedor("Fornecedor Alpha", "11.111.111/0001-11");
        Fornecedor fornecedorB = criarFornecedor("Fornecedor Beta", "22.222.222/0001-22");

        Contrato contratoA = new Contrato();
        contratoA.setDescricao("Manutencao dos elevadores");
        contratoA.setValor(new BigDecimal("1500.00"));
        contratoA.setDataInicio(LocalDate.of(2026, 1, 1));
        contratoA.setDataFim(LocalDate.of(2026, 6, 30));
        contratoA.setFornecedor(fornecedorA);
        contratoA.setCondominio(condominio);
        contratoRepository.save(contratoA);

        Contrato contratoB = new Contrato();
        contratoB.setDescricao("Seguranca eletrônica");
        contratoB.setValor(new BigDecimal("2100.00"));
        contratoB.setDataInicio(LocalDate.of(2026, 7, 1));
        contratoB.setDataFim(LocalDate.of(2026, 12, 31));
        contratoB.setFornecedor(fornecedorB);
        contratoB.setCondominio(condominio);
        contratoRepository.save(contratoB);

        mockMvc.perform(get("/contratos")
                        .param("fornecedorId", fornecedorA.getId().toString())
                        .param("dataInicio", "2026-04-01")
                        .param("dataFim", "2026-04-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].descricao").value("Manutencao dos elevadores"))
                .andExpect(jsonPath("$.content[0].fornecedorId").value(fornecedorA.getId().toString()));
    }

    @Test
    void deveFiltrarContasBancariasPorTipoEBanco() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominio("Condominio Contas");

        ContaBancaria corrente = new ContaBancaria();
        corrente.setBanco("Banco do Brasil");
        corrente.setAgencia("1234");
        corrente.setConta("0001-9");
        corrente.setTipo(TipoContaEnum.CORRENTE);
        corrente.setCondominio(condominio);
        contaBancariaRepository.save(corrente);

        ContaBancaria poupanca = new ContaBancaria();
        poupanca.setBanco("Caixa Economica");
        poupanca.setAgencia("4321");
        poupanca.setConta("9999-0");
        poupanca.setTipo(TipoContaEnum.POUPANCA);
        poupanca.setCondominio(condominio);
        contaBancariaRepository.save(poupanca);

        mockMvc.perform(get("/contas-bancarias")
                        .param("tipo", "CORRENTE")
                        .param("banco", "Brasil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].banco").value("Banco do Brasil"))
                .andExpect(jsonPath("$.content[0].tipo").value("CORRENTE"));
    }

    @Test
    void deveFiltrarUnidadesPorBlocoTipoENumero() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominio("Condominio Unidades");
        Bloco blocoA = criarBloco(condominio, "Bloco A");
        Bloco blocoB = criarBloco(condominio, "Bloco B");

        Unidade unidadeA = new Unidade();
        unidadeA.setNumero("203");
        unidadeA.setAndar("2");
        unidadeA.setTipo("APARTAMENTO");
        unidadeA.setBloco(blocoA);
        unidadeRepository.save(unidadeA);

        Unidade unidadeB = new Unidade();
        unidadeB.setNumero("101");
        unidadeB.setAndar("1");
        unidadeB.setTipo("SALA");
        unidadeB.setBloco(blocoB);
        unidadeRepository.save(unidadeB);

        mockMvc.perform(get("/unidades")
                        .param("blocoId", blocoA.getId().toString())
                        .param("tipo", "APARTAMENTO")
                        .param("numero", "203"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].numero").value("203"))
                .andExpect(jsonPath("$.content[0].tipo").value("APARTAMENTO"));
    }

    @Test
    void deveFiltrarFornecedoresPorNomeECnpj() throws Exception {
        limparBaseDeTestes();

        Condominio condominio = criarCondominio("Condominio Fornecedores");
        Fornecedor alpha = criarFornecedor("Alpha Elevadores", "12.345.678/0001-90");
        Fornecedor beta = criarFornecedor("Beta Limpeza", "98.765.432/0001-10");

        Contrato contratoAlpha = new Contrato();
        contratoAlpha.setDescricao("Contrato Alpha");
        contratoAlpha.setValor(new BigDecimal("1000.00"));
        contratoAlpha.setDataInicio(LocalDate.of(2026, 1, 1));
        contratoAlpha.setDataFim(LocalDate.of(2026, 12, 31));
        contratoAlpha.setFornecedor(alpha);
        contratoAlpha.setCondominio(condominio);
        contratoRepository.save(contratoAlpha);

        Contrato contratoBeta = new Contrato();
        contratoBeta.setDescricao("Contrato Beta");
        contratoBeta.setValor(new BigDecimal("800.00"));
        contratoBeta.setDataInicio(LocalDate.of(2026, 1, 1));
        contratoBeta.setDataFim(LocalDate.of(2026, 12, 31));
        contratoBeta.setFornecedor(beta);
        contratoBeta.setCondominio(condominio);
        contratoRepository.save(contratoBeta);

        mockMvc.perform(get("/fornecedores")
                        .param("nome", "Alpha")
                        .param("cnpj", "12.345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].nome").value("Alpha Elevadores"))
                .andExpect(jsonPath("$.content[0].cnpj").value("12.345.678/0001-90"));
    }

    private Cobranca criarCobranca(String competencia, String valor) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(new BigDecimal(valor));
        cobranca.setVencimento(LocalDate.of(2026, 3, 20));
        cobranca.setStatus(StatusCobrancaEnum.ABERTA);
        cobranca.setCompetencia(competencia);
        return cobrancaRepository.save(cobranca);
    }

    private Condominio criarCondominio(String nome) {
        Condominio condominio = new Condominio();
        condominio.setNome(nome);
        condominio.setCnpj(UUID.randomUUID().toString().substring(0, 14));
        condominio.setTelefone("(83) 99999-0000");
        return condominioRepository.save(condominio);
    }

    private Bloco criarBloco(Condominio condominio, String nome) {
        Bloco bloco = new Bloco();
        bloco.setNome(nome);
        bloco.setCondominio(condominio);
        return blocoRepository.save(bloco);
    }

    private Fornecedor criarFornecedor(String nome, String cnpj) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(nome);
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone("(83) 98888-0000");
        return fornecedorRepository.save(fornecedor);
    }
}
