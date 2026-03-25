package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.enums.PerfilEnum;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.support.SecuredIntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MeuContextoIntegrationTest extends SecuredIntegrationTestSupport {

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

    @Test
    void deveRetornarMeuContextoComAcessosDoUsuarioAutenticado() throws Exception {
        Usuario usuario = criarUsuario("contexto@email.com");
        Unidade unidade = criarUnidade("301", "Condominio Horizonte");

        Acesso acesso = new Acesso();
        acesso.setUsuario(usuario);
        acesso.setPerfil(PerfilEnum.MORADOR);
        acesso.setCondominio(unidade.getBloco().getCondominio());
        acesso.setUnidade(unidade);
        acessoRepository.save(acesso);

        unidade.setMorador(usuario);
        unidadeRepository.save(unidade);

        mockMvc.perform(get("/meu-contexto")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject(usuario.getEmail())
                                        .claim("preferred_username", usuario.getEmail()))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuario.getId().toString()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()))
                .andExpect(jsonPath("$.acessos[0].perfil").value("MORADOR"))
                .andExpect(jsonPath("$.acessos[0].condominioId").value(unidade.getBloco().getCondominio().getId().toString()))
                .andExpect(jsonPath("$.acessos[0].condominioNome").value("Condominio Horizonte"))
                .andExpect(jsonPath("$.acessos[0].unidadeId").value(unidade.getId().toString()))
                .andExpect(jsonPath("$.acessos[0].unidadeNumero").value("301"));
    }

    @Test
    void deveRetornar404QuandoUsuarioAutenticadoNaoEstiverCadastrado() throws Exception {
        mockMvc.perform(get("/meu-contexto")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject("nao.existe@email.com")
                                        .claim("preferred_username", "nao.existe@email.com"))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario autenticado não encontrado"));
    }

    @Test
    void deveVincularExternalIdDoTokenAoUsuarioEncontradoPorEmail() throws Exception {
        Usuario usuario = criarUsuario("vinculo@email.com");

        mockMvc.perform(get("/meu-contexto")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject("keycloak-user-123")
                                        .claim("email", usuario.getEmail())
                                        .claim("preferred_username", usuario.getEmail()))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuario.getId().toString()));

        Usuario usuarioAtualizado = usuarioRepository.findById(usuario.getId()).orElseThrow();
        assertEquals("keycloak-user-123", usuarioAtualizado.getExternalId());
    }

    private Usuario criarUsuario(String email) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Contexto");
        usuario.setEmail(email);
        usuario.setTelefone("(83) 99999-0000");
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    private Unidade criarUnidade(String numero, String nomeCondominio) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Sessao");
        endereco.setNumero("50");
        endereco.setComplemento("Apto");
        endereco.setBairro("Centro");
        endereco.setCidade("Joao Pessoa");
        endereco.setEstado("PB");
        endereco.setCep("58000-200");
        endereco = enderecoRepository.save(endereco);

        Condominio condominio = new Condominio();
        condominio.setNome(nomeCondominio);
        condominio.setCnpj("98.765.432/0001-" + numero);
        condominio.setTelefone("(83) 97777-0000");
        condominio.setEndereco(endereco);
        condominio = condominioRepository.save(condominio);

        Bloco bloco = new Bloco();
        bloco.setNome("Bloco " + numero);
        bloco.setCondominio(condominio);
        bloco = blocoRepository.save(bloco);

        Unidade unidade = new Unidade();
        unidade.setNumero(numero);
        unidade.setAndar("3");
        unidade.setTipo("Apartamento");
        unidade.setBloco(bloco);
        return unidadeRepository.save(unidade);
    }
}
