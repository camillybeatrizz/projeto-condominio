package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AcessoIntegrationTest extends IntegrationTestSupport {

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

    @Test
    void deveCriarAcessoDeMoradorComCondominioEUnidade() throws Exception {
        Usuario usuario = criarUsuario("morador.vinculado@email.com");
        Unidade unidade = criarUnidade("101", "Condominio Central");

        String payload = """
                {
                  "usuarioId": "%s",
                  "condominioId": "%s",
                  "unidadeId": "%s",
                  "perfil": "MORADOR"
                }
                """.formatted(usuario.getId(), unidade.getBloco().getCondominio().getId(), unidade.getId());

        mockMvc.perform(post("/acessos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuarioId").value(usuario.getId().toString()))
                .andExpect(jsonPath("$.condominioId").value(unidade.getBloco().getCondominio().getId().toString()))
                .andExpect(jsonPath("$.unidadeId").value(unidade.getId().toString()))
                .andExpect(jsonPath("$.perfil").value("MORADOR"));

        Unidade unidadeAtualizada = unidadeRepository.findById(unidade.getId()).orElseThrow();
        assertThat(unidadeAtualizada.getMorador()).isNotNull();
        assertThat(unidadeAtualizada.getMorador().getId()).isEqualTo(usuario.getId());
    }

    @Test
    void deveRetornar400QuandoSindicoNaoInformarCondominio() throws Exception {
        Usuario usuario = criarUsuario("sindico.sem.condominio@email.com");

        String payload = """
                {
                  "usuarioId": "%s",
                  "perfil": "SINDICO"
                }
                """.formatted(usuario.getId());

        mockMvc.perform(post("/acessos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Condominio é obrigatório para perfis SINDICO e MORADOR"));
    }

    @Test
    void deveDesvincularMoradorDaUnidadeAoRemoverAcesso() throws Exception {
        Usuario usuario = criarUsuario("morador.remocao@email.com");
        Unidade unidade = criarUnidade("202", "Condominio Jardim");

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

        Unidade unidadeAtualizada = unidadeRepository.findById(unidade.getId()).orElseThrow();
        assertThat(unidadeAtualizada.getMorador()).isNull();
    }

    private Usuario criarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Teste");
        usuario.setEmail(email);
        usuario.setTelefone("(83) 99999-9999");
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    private Unidade criarUnidade(String numero, String nomeCondominio) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Estrutura");
        endereco.setNumero("10");
        endereco.setComplemento("Apto");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-000");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nomeCondominio);
        condominio.setCnpj("12.345.678/0001-" + numero);
        condominio.setTelefone("(83) 98888-0000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + numero);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero(numero);
        unidade.setAndar("2");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        return unidadeRepository.save(unidade);
    }
}
