package com.codewithus.kondo;

import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.support.DatabaseCleanupSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "kondo.security.enabled=true",
        "kondo.security.strict-external-identity=true",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://issuer.test/kondo",
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://issuer.test/.well-known/jwks.json"
})
@AutoConfigureMockMvc
class StrictExternalIdentityIntegrationTest extends DatabaseCleanupSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveNegarUsuarioSemExternalIdQuandoModoEstritoEstiverAtivo() throws Exception {
        Usuario usuario = criarUsuario("estrito@email.com", null);

        mockMvc.perform(get("/meu-contexto")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject("keycloak-subject-001")
                                        .claim("email", usuario.getEmail()))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuario autenticado não encontrado"));
    }

    @Test
    void deveAceitarUsuarioComExternalIdQuandoModoEstritoEstiverAtivo() throws Exception {
        Usuario usuario = criarUsuario("estrito.vinculado@email.com", "keycloak-subject-002");

        mockMvc.perform(get("/meu-contexto")
                        .with(jwt()
                                .jwt(token -> token
                                        .subject("keycloak-subject-002")
                                        .claim("email", usuario.getEmail()))
                                .authorities(new SimpleGrantedAuthority("ROLE_MORADOR"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(usuario.getId().toString()))
                .andExpect(jsonPath("$.email").value(usuario.getEmail()));
    }

    private Usuario criarUsuario(String email, String externalId) {
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Estrito");
        usuario.setEmail(email);
        usuario.setExternalId(externalId);
        usuario.setTelefone("(83) 99999-0000");
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }
}
