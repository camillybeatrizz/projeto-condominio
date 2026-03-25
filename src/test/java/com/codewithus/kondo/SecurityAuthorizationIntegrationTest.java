package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Contrato;
import com.codewithus.kondo.domain.entity.ContaBancaria;
import com.codewithus.kondo.domain.entity.Despesa;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import com.codewithus.kondo.domain.enums.PerfilEnum;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.domain.enums.TipoContaEnum;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.ContaBancariaRepository;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.DespesaRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.support.SecuredIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityAuthorizationIntegrationTest extends SecuredIntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private AcessoRepository acessoRepository;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void adminDeveConseguirCriarUsuario() throws Exception {
        String payload = """
                {
                  "nome": "Admin User",
                  "email": "admin.user@email.com",
                  "telefone": "(83) 99999-9999",
                  "ativo": true
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void sindicoNaoDeveConseguirCriarUsuario() throws Exception {
        String payload = """
                {
                  "nome": "Sindico User",
                  "email": "sindico.user@email.com",
                  "telefone": "(83) 99999-9999",
                  "ativo": true
                }
                """;

        mockMvc.perform(post("/usuarios")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_SINDICO")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void moradorDeveConseguirListarCondominios() throws Exception {
        mockMvc.perform(get("/condominios")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk());
    }

    @Test
    void moradorNaoDeveConseguirCriarPagamento() throws Exception {
        String payload = """
                {
                  "valor": 350.00,
                  "dataPagamento": "2026-03-18",
                  "forma": "PIX",
                  "transactionId": "txn-forbidden-001",
                  "cobrancaId": "11111111-1111-1111-1111-111111111111"
                }
                """;

        mockMvc.perform(post("/pagamentos")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_MORADOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void sindicoDeveConseguirCriarCondominio() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Segura");
        endereco.setNumero("100");
        endereco.setComplemento("Sala 1");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-000");
        endereco = enderecoRepository.save(endereco);

        String payload = """
                {
                  "nome": "Condominio Seguro",
                  "cnpj": "99.888.777/0001-66",
                  "telefone": "(83) 99999-1234",
                  "enderecoId": "%s"
                }
                """.formatted(endereco.getId());

        mockMvc.perform(post("/condominios")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_SINDICO")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void moradorDeveConseguirBuscarPropriaCobranca() throws Exception {
        Unidade unidade = criarUnidadeComMorador("morador1@email.com", "101");
        Cobranca cobranca = criarCobranca(unidade, "2026-06");

        mockMvc.perform(get("/cobrancas/{id}", cobranca.getId())
                        .with(jwt()
                                .jwt(token -> token.subject("morador1@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk());
    }

    @Test
    void moradorNaoDeveConseguirBuscarCobrancaDeOutraUnidade() throws Exception {
        Unidade unidade = criarUnidadeComMorador("morador2@email.com", "102");
        Cobranca cobranca = criarCobranca(unidade, "2026-07");

        mockMvc.perform(get("/cobrancas/{id}", cobranca.getId())
                        .with(jwt()
                                .jwt(token -> token.subject("morador1@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void moradorDeveConseguirCriarChamadoNaPropriaUnidade() throws Exception {
        Unidade unidade = criarUnidadeComMorador("morador3@email.com", "103");

        String payload = """
                {
                  "descricao": "Portao com defeito",
                  "status": "ABERTO",
                  "dataAbertura": "2026-03-18",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        mockMvc.perform(post("/chamados")
                        .with(jwt()
                                .jwt(token -> token.subject("morador3@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void moradorNaoDeveConseguirCriarChamadoEmUnidadeDeOutroMorador() throws Exception {
        Unidade unidade = criarUnidadeComMorador("morador4@email.com", "104");

        String payload = """
                {
                  "descricao": "Luz queimada",
                  "status": "ABERTO",
                  "dataAbertura": "2026-03-18",
                  "unidadeId": "%s"
                }
                """.formatted(unidade.getId());

        mockMvc.perform(post("/chamados")
                        .with(jwt()
                                .jwt(token -> token.subject("morador5@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void sindicoDeveListarApenasCondominiosDoSeuEscopo() throws Exception {
        Condominio condominioPermitido = criarCondominio("201", "Condominio Permitido");
        criarCondominio("202", "Condominio Fora");
        vincularSindicoAoCondominio("sindico.escopo@email.com", condominioPermitido);

        mockMvc.perform(get("/condominios")
                        .param("page", "0")
                        .param("size", "10")
                        .with(jwt()
                                .jwt(token -> token.subject("sindico.escopo@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_SINDICO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(condominioPermitido.getId().toString()));
    }

    @Test
    void sindicoNaoDeveConseguirBuscarCobrancaDeOutroCondominio() throws Exception {
        Unidade unidade = criarUnidadeSemMorador("301", "Condominio Outro");
        Cobranca cobranca = criarCobranca(unidade, "2026-08");

        mockMvc.perform(get("/cobrancas/{id}", cobranca.getId())
                        .with(jwt()
                                .jwt(token -> token.subject("sindico.sem.escopo@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_SINDICO"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void sindicoDeveConseguirBuscarCobrancaDoSeuCondominio() throws Exception {
        Unidade unidade = criarUnidadeSemMorador("302", "Condominio Vinculado");
        Cobranca cobranca = criarCobranca(unidade, "2026-09");
        vincularSindicoAoCondominio("sindico.vinculado@email.com", unidade.getBloco().getCondominio());

        mockMvc.perform(get("/cobrancas/{id}", cobranca.getId())
                        .with(jwt()
                                .jwt(token -> token.subject("sindico.vinculado@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_SINDICO"))))
                .andExpect(status().isOk());
    }

    @Test
    void sindicoDeveListarApenasContasBancariasDoSeuEscopo() throws Exception {
        Condominio condominioPermitido = criarCondominio("401", "Condominio Conta");
        Condominio condominioFora = criarCondominio("402", "Condominio Conta Fora");
        criarContaBancaria(condominioPermitido, "001");
        criarContaBancaria(condominioFora, "002");
        vincularSindicoAoCondominio("sindico.conta@email.com", condominioPermitido);

        mockMvc.perform(get("/contas-bancarias")
                        .param("page", "0")
                        .param("size", "10")
                        .with(jwt()
                                .jwt(token -> token.subject("sindico.conta@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_SINDICO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void sindicoNaoDeveConseguirBuscarContratoDeOutroCondominio() throws Exception {
        Condominio condominio = criarCondominio("403", "Condominio Contrato");
        Fornecedor fornecedor = criarFornecedor("Fornecedor Escopo");
        Contrato contrato = criarContrato(condominio, fornecedor, "Contrato Fora");

        mockMvc.perform(get("/contratos/{id}", contrato.getId())
                        .with(jwt()
                                .jwt(token -> token.subject("sindico.contrato@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_SINDICO"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void sindicoDeveListarApenasFornecedoresComContratoNoSeuEscopo() throws Exception {
        Condominio condominioPermitido = criarCondominio("404", "Condominio Fornecedor");
        Condominio condominioFora = criarCondominio("405", "Condominio Fornecedor Fora");
        Fornecedor fornecedorPermitido = criarFornecedor("Fornecedor Permitido");
        Fornecedor fornecedorFora = criarFornecedor("Fornecedor Fora");
        criarContrato(condominioPermitido, fornecedorPermitido, "Contrato Permitido");
        criarContrato(condominioFora, fornecedorFora, "Contrato Fora");
        vincularSindicoAoCondominio("sindico.fornecedor@email.com", condominioPermitido);

        mockMvc.perform(get("/fornecedores")
                        .param("page", "0")
                        .param("size", "10")
                        .with(jwt()
                                .jwt(token -> token.subject("sindico.fornecedor@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_SINDICO"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(fornecedorPermitido.getId().toString()));
    }

    @Test
    void moradorDeveListarApenasDespesasDoSeuCondominio() throws Exception {
        Unidade unidadeMorador = criarUnidadeComMorador("morador.despesa@email.com", "501");
        Condominio condominioPermitido = unidadeMorador.getBloco().getCondominio();
        Condominio condominioFora = criarCondominio("502", "Condominio Despesa Fora");

        Despesa despesaPermitida = new Despesa();
        despesaPermitida.setDescricao("Pintura da fachada");
        despesaPermitida.setValor(new BigDecimal("1800.00"));
        despesaPermitida.setData(LocalDate.of(2026, 3, 10));
        despesaPermitida.setCategoria(CategoriaDespesaEnum.MANUTENCAO);
        despesaPermitida.setCondominio(condominioPermitido);
        despesaRepository.save(despesaPermitida);

        Despesa despesaFora = new Despesa();
        despesaFora.setDescricao("Troca de cameras");
        despesaFora.setValor(new BigDecimal("2400.00"));
        despesaFora.setData(LocalDate.of(2026, 3, 12));
        despesaFora.setCategoria(CategoriaDespesaEnum.SEGURANCA);
        despesaFora.setCondominio(condominioFora);
        despesaRepository.save(despesaFora);

        mockMvc.perform(get("/despesas")
                        .param("page", "0")
                        .param("size", "10")
                        .with(jwt()
                                .jwt(token -> token.subject("morador.despesa@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].descricao").value("Pintura da fachada"));
    }

    @Test
    void moradorDeveListarApenasFornecedoresComContratoNoSeuCondominio() throws Exception {
        Unidade unidadeMorador = criarUnidadeComMorador("morador.fornecedor@email.com", "503");
        Condominio condominioPermitido = unidadeMorador.getBloco().getCondominio();
        Condominio condominioFora = criarCondominio("504", "Condominio Fornecedor Fora");

        Fornecedor fornecedorPermitido = criarFornecedor("Fornecedor Morador Permitido");
        Fornecedor fornecedorFora = criarFornecedor("Fornecedor Morador Fora");
        criarContrato(condominioPermitido, fornecedorPermitido, "Contrato Permitido Morador");
        criarContrato(condominioFora, fornecedorFora, "Contrato Fora Morador");

        mockMvc.perform(get("/fornecedores")
                        .param("page", "0")
                        .param("size", "10")
                        .with(jwt()
                                .jwt(token -> token.subject("morador.fornecedor@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(fornecedorPermitido.getId().toString()));
    }

    private Unidade criarUnidadeComMorador(String emailMorador, String numeroUnidade) {
        Usuario morador = new Usuario();
        morador.setNome("Morador " + numeroUnidade);
        morador.setEmail(emailMorador);
        morador.setTelefone("(83) 99999-0000");
        morador.setAtivo(true);
        morador = usuarioRepository.save(morador);

        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Base " + numeroUnidade);
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-100");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome("Condominio " + numeroUnidade);
        condominio.setCnpj("10.000.000/0001-" + numeroUnidade);
        condominio.setTelefone("(83) 99999-1000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + numeroUnidade);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero(numeroUnidade);
        unidade.setAndar("1");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        unidade.setMorador(morador);
        return unidadeRepository.save(unidade);
    }

    private Unidade criarUnidadeSemMorador(String numeroUnidade, String nomeCondominio) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Base " + numeroUnidade);
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-100");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nomeCondominio);
        condominio.setCnpj("20.000.000/0001-" + numeroUnidade);
        condominio.setTelefone("(83) 99999-1000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + numeroUnidade);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero(numeroUnidade);
        unidade.setAndar("1");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        return unidadeRepository.save(unidade);
    }

    private Condominio criarCondominio(String numero, String nome) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Escopo " + numero);
        endereco.setNumero("50");
        endereco.setComplemento("Sala");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-200");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nome);
        condominio.setCnpj("30.000.000/0001-" + numero);
        condominio.setTelefone("(83) 98888-1000");
        condominio.setEndereco(endereco);
        return condominioRepository.save(condominio);
    }

    private void vincularSindicoAoCondominio(String email, Condominio condominio) {
        Usuario sindico = new Usuario();
        sindico.setNome("Sindico " + condominio.getNome());
        sindico.setEmail(email);
        sindico.setTelefone("(83) 97777-0000");
        sindico.setAtivo(true);
        sindico = usuarioRepository.save(sindico);

        Acesso acesso = new Acesso();
        acesso.setUsuario(sindico);
        acesso.setPerfil(PerfilEnum.SINDICO);
        acesso.setCondominio(condominio);
        acessoRepository.save(acesso);
    }

    private ContaBancaria criarContaBancaria(Condominio condominio, String sufixo) {
        ContaBancaria conta = new ContaBancaria();
        conta.setBanco("Banco " + sufixo);
        conta.setAgencia("000" + sufixo);
        conta.setConta("12345-" + sufixo);
        conta.setTipo(TipoContaEnum.CORRENTE);
        conta.setCondominio(condominio);
        return contaBancariaRepository.save(conta);
    }

    private Fornecedor criarFornecedor(String nome) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(nome);
        fornecedor.setCnpj("99.999.999/0001-" + Math.abs(nome.hashCode() % 90 + 10));
        fornecedor.setTelefone("(83) 96666-0000");
        return fornecedorRepository.save(fornecedor);
    }

    private Contrato criarContrato(Condominio condominio, Fornecedor fornecedor, String descricao) {
        Contrato contrato = new Contrato();
        contrato.setDescricao(descricao);
        contrato.setValor(new BigDecimal("1200.00"));
        contrato.setDataInicio(LocalDate.of(2026, 1, 1));
        contrato.setDataFim(LocalDate.of(2026, 12, 31));
        contrato.setCondominio(condominio);
        contrato.setFornecedor(fornecedor);
        return contratoRepository.save(contrato);
    }

    private Cobranca criarCobranca(Unidade unidade, String competencia) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(new BigDecimal("250.00"));
        cobranca.setVencimento(LocalDate.of(2026, 6, 20));
        cobranca.setStatus(StatusCobrancaEnum.ABERTA);
        cobranca.setCompetencia(competencia);
        cobranca.setUnidade(unidade);
        return cobrancaRepository.save(cobranca);
    }
}
