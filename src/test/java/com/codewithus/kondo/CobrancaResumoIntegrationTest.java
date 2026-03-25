package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.enums.PerfilEnum;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.support.SecuredIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CobrancaResumoIntegrationTest extends SecuredIntegrationTestSupport {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private AcessoRepository acessoRepository;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Test
    void deveRestringirResumoAoEscopoDoMoradorAutenticado() throws Exception {
        limparBaseDeTestes();

        Usuario morador = criarUsuario("morador.resumo@email.com");
        Unidade unidadeMorador = criarUnidade("501", "Condominio Resumo");
        vincularMorador(morador, unidadeMorador);

        Cobranca cobrancaAberta = new Cobranca();
        cobrancaAberta.setValor(new BigDecimal("300.00"));
        cobrancaAberta.setVencimento(LocalDate.now().plusDays(2));
        cobrancaAberta.setStatus(StatusCobrancaEnum.ABERTA);
        cobrancaAberta.setCompetencia("2026-03");
        cobrancaAberta.setUnidade(unidadeMorador);
        cobrancaRepository.save(cobrancaAberta);

        Cobranca cobrancaVencida = new Cobranca();
        cobrancaVencida.setValor(new BigDecimal("150.00"));
        cobrancaVencida.setVencimento(LocalDate.now().minusDays(2));
        cobrancaVencida.setStatus(StatusCobrancaEnum.VENCIDA);
        cobrancaVencida.setCompetencia("2026-02");
        cobrancaVencida.setUnidade(unidadeMorador);
        cobrancaRepository.save(cobrancaVencida);

        Unidade outraUnidade = criarUnidade("601", "Condominio Vizinho");
        Cobranca cobrancaOutraPessoa = new Cobranca();
        cobrancaOutraPessoa.setValor(new BigDecimal("999.00"));
        cobrancaOutraPessoa.setVencimento(LocalDate.now().plusDays(10));
        cobrancaOutraPessoa.setStatus(StatusCobrancaEnum.ABERTA);
        cobrancaOutraPessoa.setCompetencia("2026-04");
        cobrancaOutraPessoa.setUnidade(outraUnidade);
        cobrancaRepository.save(cobrancaOutraPessoa);

        mockMvc.perform(get("/cobrancas/resumo")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject("sub-morador-resumo")
                                        .claim("email", morador.getEmail())
                                        .claim("preferred_username", morador.getEmail()))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCobrancas").value(2))
                .andExpect(jsonPath("$.totalAbertas").value(1))
                .andExpect(jsonPath("$.totalPagas").value(0))
                .andExpect(jsonPath("$.totalInadimplentes").value(1))
                .andExpect(jsonPath("$.valorTotal").value(450.00))
                .andExpect(jsonPath("$.valorAberto").value(300.00))
                .andExpect(jsonPath("$.valorPago").value(0))
                .andExpect(jsonPath("$.valorInadimplente").value(150.00));
    }

    @Test
    void deveRestringirDashboardAoEscopoDoMoradorAutenticado() throws Exception {
        limparBaseDeTestes();

        Usuario morador = criarUsuario("morador.dashboard@email.com");
        Unidade unidadeMorador = criarUnidade("701", "Condominio Dashboard");
        vincularMorador(morador, unidadeMorador);

        Cobranca cobrancaAberta = new Cobranca();
        cobrancaAberta.setValor(new BigDecimal("220.00"));
        cobrancaAberta.setVencimento(LocalDate.now().plusDays(1));
        cobrancaAberta.setStatus(StatusCobrancaEnum.ABERTA);
        cobrancaAberta.setCompetencia("2026-03");
        cobrancaAberta.setUnidade(unidadeMorador);
        cobrancaAberta = cobrancaRepository.save(cobrancaAberta);

        Cobranca cobrancaVencida = new Cobranca();
        cobrancaVencida.setValor(new BigDecimal("120.00"));
        cobrancaVencida.setVencimento(LocalDate.now().minusDays(1));
        cobrancaVencida.setStatus(StatusCobrancaEnum.VENCIDA);
        cobrancaVencida.setCompetencia("2026-02");
        cobrancaVencida.setUnidade(unidadeMorador);
        cobrancaVencida = cobrancaRepository.save(cobrancaVencida);

        Unidade outraUnidade = criarUnidade("702", "Outro Condominio Dashboard");
        Cobranca cobrancaOutraPessoa = new Cobranca();
        cobrancaOutraPessoa.setValor(new BigDecimal("888.00"));
        cobrancaOutraPessoa.setVencimento(LocalDate.now().plusDays(10));
        cobrancaOutraPessoa.setStatus(StatusCobrancaEnum.ABERTA);
        cobrancaOutraPessoa.setCompetencia("2026-05");
        cobrancaOutraPessoa.setUnidade(outraUnidade);
        cobrancaRepository.save(cobrancaOutraPessoa);

        mockMvc.perform(get("/cobrancas/dashboard")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject("sub-morador-dashboard")
                                        .claim("email", morador.getEmail())
                                        .claim("preferred_username", morador.getEmail()))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resumo.totalCobrancas").value(2))
                .andExpect(jsonPath("$.resumo.valorTotal").value(340.00))
                .andExpect(jsonPath("$.inadimplentesRecentes", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$.inadimplentesRecentes[0].id").value(cobrancaVencida.getId().toString()))
                .andExpect(jsonPath("$.pagamentosRecentes", org.hamcrest.Matchers.hasSize(0)));
    }

    private Usuario criarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("Morador Resumo");
        usuario.setEmail(email);
        usuario.setTelefone("(85) 99999-0000");
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    private Unidade criarUnidade(String numero, String nomeCondominio) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Resumo");
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Fortaleza");
        endereco.setEstado("CE");
        endereco.setCep("60000-000");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nomeCondominio);
        condominio.setCnpj(java.util.UUID.randomUUID().toString().substring(0, 14));
        condominio.setTelefone("(85) 97777-0000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + numero);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero(numero);
        unidade.setAndar("5");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        return unidadeRepository.save(unidade);
    }

    private void vincularMorador(Usuario usuario, Unidade unidade) {
        Acesso acesso = new Acesso();
        acesso.setUsuario(usuario);
        acesso.setPerfil(PerfilEnum.MORADOR);
        acesso.setCondominio(unidade.getBloco().getCondominio());
        acesso.setUnidade(unidade);
        acessoRepository.save(acesso);

        unidade.setMorador(usuario);
        unidadeRepository.save(unidade);
    }
}
