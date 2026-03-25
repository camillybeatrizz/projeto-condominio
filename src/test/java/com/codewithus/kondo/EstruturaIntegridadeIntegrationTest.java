package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.enums.PerfilEnum;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EstruturaIntegridadeIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CondominioRepository condominioRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AcessoRepository acessoRepository;

    @Test
    void deveRetornar409QuandoCriarBlocoDuplicadoNoMesmoCondominio() throws Exception {
        Condominio condominio = criarCondominio("101", "Condominio Duplicado");

        String payload = """
                {
                  "nome": "Bloco A",
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        mockMvc.perform(post("/blocos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/blocos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe bloco com este nome no condomínio informado"));
    }

    @Test
    void deveRetornar409QuandoCriarUnidadeDuplicadaNoMesmoBloco() throws Exception {
        Condominio condominio = criarCondominio("102", "Condominio Unidade");
        Bloco bloco = criarBloco(condominio, "Bloco B");

        String payload = """
                {
                  "numero": "101",
                  "andar": "1",
                  "tipo": "Apartamento",
                  "blocoId": "%s"
                }
                """.formatted(bloco.getId());

        mockMvc.perform(post("/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe unidade com este número no bloco informado"));
    }

    @Test
    void deveRetornar400AoExcluirCondominioComBlocosVinculados() throws Exception {
        Condominio condominio = criarCondominio("103", "Condominio Estrutural");
        criarBloco(condominio, "Bloco C");

        mockMvc.perform(delete("/condominios/{id}", condominio.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir condomínio que ainda possui blocos vinculados"));
    }

    @Test
    void deveRealizarExclusaoLogicaDeCondominioSemDependencias() throws Exception {
        Condominio condominio = criarCondominio("109", "Condominio Arquivado");

        mockMvc.perform(delete("/condominios/{id}", condominio.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/condominios/{id}", condominio.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Condominio não encontrado"));

        Condominio persistido = condominioRepository.findById(condominio.getId()).orElseThrow();
        assertThat(persistido.getDeletedAt()).isNotNull();
        assertThat(persistido.getDeletedBy()).isEqualTo("SYSTEM");
    }

    @Test
    void devePermitirReutilizarCnpjDeCondominioAposExclusaoLogica() throws Exception {
        Endereco endereco = criarEndereco("Rua Reuso Condominio", "58000-555");

        String payload = """
                {
                  "nome": "Condominio Reutilizavel",
                  "cnpj": "90.000.000/0001-90",
                  "telefone": "(83) 99999-9090",
                  "enderecoId": "%s"
                }
                """.formatted(endereco.getId());

        String response = postAndReturnBody("/condominios", payload);
        String condominioId = jsonField(response, "id");

        mockMvc.perform(delete("/condominios/{id}", condominioId))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cnpj").value("90.000.000/0001-90"));
    }

    @Test
    void deveRetornar400AoExcluirBlocoComUnidadesVinculadas() throws Exception {
        Condominio condominio = criarCondominio("104", "Condominio Bloco");
        Bloco bloco = criarBloco(condominio, "Bloco D");

        String payload = """
                {
                  "numero": "202",
                  "andar": "2",
                  "tipo": "Apartamento",
                  "blocoId": "%s"
                }
                """.formatted(bloco.getId());

        mockMvc.perform(post("/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/blocos/{id}", bloco.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir bloco que ainda possui unidades vinculadas"));
    }

    @Test
    void deveRealizarExclusaoLogicaDeBlocoSemUnidades() throws Exception {
        Condominio condominio = criarCondominio("110", "Condominio Bloco Arquivado");
        Bloco bloco = criarBloco(condominio, "Bloco Livre");

        mockMvc.perform(delete("/blocos/{id}", bloco.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/blocos/{id}", bloco.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Bloco não encontrado"));

        Bloco persistido = blocoRepository.findById(bloco.getId()).orElseThrow();
        assertThat(persistido.getDeletedAt()).isNotNull();
        assertThat(persistido.getDeletedBy()).isEqualTo("SYSTEM");
    }

    @Test
    void devePermitirReutilizarNomeDeBlocoNoMesmoCondominioAposExclusaoLogica() throws Exception {
        Condominio condominio = criarCondominio("112", "Condominio Reuso Bloco");

        String payload = """
                {
                  "nome": "Bloco Reutilizavel",
                  "condominioId": "%s"
                }
                """.formatted(condominio.getId());

        String response = postAndReturnBody("/blocos", payload);
        String blocoId = jsonField(response, "id");

        mockMvc.perform(delete("/blocos/{id}", blocoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/blocos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Bloco Reutilizavel"));
    }

    @Test
    void deveRetornar400AoExcluirUnidadeComCobrancasVinculadas() throws Exception {
        Condominio condominio = criarCondominio("105", "Condominio Cobranca");
        Bloco bloco = criarBloco(condominio, "Bloco E");

        String unidadePayload = """
                {
                  "numero": "303",
                  "andar": "3",
                  "tipo": "Apartamento",
                  "blocoId": "%s"
                }
                """.formatted(bloco.getId());

        String unidadeResponse = postAndReturnBody("/unidades", unidadePayload);
        String unidadeId = jsonField(unidadeResponse, "id");

        String cobrancaPayload = """
                {
                  "valor": 450.00,
                  "vencimento": "2026-06-20",
                  "status": "ABERTA",
                  "competencia": "2026-06",
                  "unidadeId": "%s"
                }
                """.formatted(unidadeId);

        mockMvc.perform(post("/cobrancas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cobrancaPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/unidades/{id}", unidadeId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir unidade que possui cobranças vinculadas"));
    }

    @Test
    void deveRealizarExclusaoLogicaDeUnidadeSemDependencias() throws Exception {
        Condominio condominio = criarCondominio("111", "Condominio Unidade Arquivada");
        Bloco bloco = criarBloco(condominio, "Bloco F");

        String unidadePayload = """
                {
                  "numero": "404",
                  "andar": "4",
                  "tipo": "Apartamento",
                  "blocoId": "%s"
                }
                """.formatted(bloco.getId());

        String unidadeResponse = postAndReturnBody("/unidades", unidadePayload);
        String unidadeId = jsonField(unidadeResponse, "id");

        mockMvc.perform(delete("/unidades/{id}", unidadeId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/unidades/{id}", unidadeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Unidade não encontrada"));

        var persistida = unidadeRepository.findById(java.util.UUID.fromString(unidadeId)).orElseThrow();
        assertThat(persistida.getDeletedAt()).isNotNull();
        assertThat(persistida.getDeletedBy()).isEqualTo("SYSTEM");
    }

    @Test
    void devePermitirReutilizarNumeroDeUnidadeNoMesmoBlocoAposExclusaoLogica() throws Exception {
        Condominio condominio = criarCondominio("113", "Condominio Reuso Unidade");
        Bloco bloco = criarBloco(condominio, "Bloco G");

        String payload = """
                {
                  "numero": "505",
                  "andar": "5",
                  "tipo": "Apartamento",
                  "blocoId": "%s"
                }
                """.formatted(bloco.getId());

        String response = postAndReturnBody("/unidades", payload);
        String unidadeId = jsonField(response, "id");

        mockMvc.perform(delete("/unidades/{id}", unidadeId))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/unidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("505"));
    }

    @Test
    void deveRetornar400AoExcluirUsuarioComAcessosVinculados() throws Exception {
        Condominio condominio = criarCondominio("106", "Condominio Usuario");

        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Vinculado");
        usuario.setEmail("usuario.vinculado@email.com");
        usuario.setTelefone("(83) 99999-1111");
        usuario.setAtivo(true);
        usuario = usuarioRepository.save(usuario);

        Acesso acesso = new Acesso();
        acesso.setUsuario(usuario);
        acesso.setPerfil(PerfilEnum.SINDICO);
        acesso.setCondominio(condominio);
        acessoRepository.save(acesso);

        mockMvc.perform(delete("/usuarios/{id}", usuario.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não é permitido excluir usuário que possui acessos vinculados"));
    }

    private Condominio criarCondominio(String sufixo, String nome) {
        Endereco endereco = criarEndereco("Rua Estrutura " + sufixo, "58000-100");

        Condominio condominio = new Condominio();
        condominio.setNome(nome);
        condominio.setCnpj("70.000.000/0001-" + sufixo);
        condominio.setTelefone("(83) 99999-1000");
        condominio.setEndereco(endereco);
        return condominioRepository.save(condominio);
    }

    private Bloco criarBloco(Condominio condominio, String nome) {
        Bloco bloco = new Bloco();
        bloco.setNome(nome);
        bloco.setCondominio(condominio);
        return blocoRepository.save(bloco);
    }

    private Endereco criarEndereco(String logradouro, String cep) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(logradouro);
        endereco.setNumero("10");
        endereco.setComplemento("Casa");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep(cep);
        return enderecoRepository.save(endereco);
    }
}
