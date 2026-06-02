package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.AuditoriaEvento;
import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.repository.AuditoriaEventoRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@org.springframework.boot.test.context.SpringBootTest(properties = {
        "kondo.security.enabled=false",
        "kondo.integrations.asaas.webhook.access-token=asaas-test-token",
        "kondo.integrations.asaas.webhook.allow-legacy-access-token=true"
})
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = false)
class AuditoriaIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private AuditoriaEventoRepository auditoriaEventoRepository;

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
    private CobrancaRepository cobrancaRepository;

    @Test
    void deveRegistrarAuditoriaAoCriarPagamentoViaWebhook() throws Exception {
        Cobranca cobranca = criarCobrancaAberta("audit-pay-001");

        String payload = """
                {
                  "id": "evt_audit_001",
                  "event": "PAYMENT_RECEIVED",
                  "payment": {
                    "id": "audit-pay-001",
                    "value": 350.00,
                    "billingType": "PIX",
                    "clientPaymentDate": "2026-03-25"
                  }
                }
                """;

        mockMvc.perform(post("/webhooks/asaas")
                        .header("asaas-access-token", "asaas-test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        assertThat(auditoriaEventoRepository.findAll())
                .extracting(AuditoriaEvento::getTipoEvento)
                .contains("PAGAMENTO_PROCESSADO_POR_WEBHOOK", "COBRANCA_MARCADA_PAGA");

        Cobranca cobrancaAtualizada = cobrancaRepository.findById(cobranca.getId()).orElseThrow();
        assertThat(cobrancaAtualizada.getStatus()).isEqualTo(StatusCobrancaEnum.PAGA);
    }

    @Test
    void deveRegistrarAuditoriaAoAlterarAcesso() throws Exception {
        Usuario usuario = criarUsuario("auditoria.acesso@email.com");
        Unidade unidade = criarUnidade("404", "Condominio Auditoria");

        String payload = """
                {
                  "usuarioId": "%s",
                  "condominioId": "%s",
                  "unidadeId": "%s",
                  "perfil": "MORADOR"
                }
                """.formatted(usuario.getId(), unidade.getBloco().getCondominio().getId(), unidade.getId());

        String response = postAndReturnBody("/acessos", payload);
        String acessoId = jsonField(response, "id");

        mockMvc.perform(delete("/acessos/{id}", acessoId))
                .andExpect(status().isNoContent());

        assertThat(auditoriaEventoRepository.findAll())
                .extracting(AuditoriaEvento::getTipoEvento)
                .contains("ACESSO_CRIADO", "ACESSO_EXCLUIDO");
    }

    private Usuario criarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Auditoria");
        usuario.setEmail(email);
        usuario.setTelefone("(83) 99999-0000");
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    private Unidade criarUnidade(String numero, String nomeCondominio) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Auditoria");
        endereco.setNumero("10");
        endereco.setComplemento("Apto");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-000");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nomeCondominio);
        condominio.setCnpj("90.000.000/0001-" + numero);
        condominio.setTelefone("(83) 90000-0000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + numero);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero(numero);
        unidade.setAndar("4");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        return unidadeRepository.save(unidade);
    }

    private Cobranca criarCobrancaAberta(String referenciaExterna) {
        Cobranca cobranca = new Cobranca();
        cobranca.setValor(new BigDecimal("350.00"));
        cobranca.setVencimento(LocalDate.of(2026, 3, 30));
        cobranca.setStatus(StatusCobrancaEnum.ABERTA);
        cobranca.setCompetencia("2026-03");
        cobranca.setReferenciaExterna(referenciaExterna);
        return cobrancaRepository.save(cobranca);
    }
}
