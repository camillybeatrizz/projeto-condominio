package com.codewithus.kondo.config;

import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean ativo = usuario.getAtivo() != null && usuario.getAtivo();

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                ativo,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_USER")) //Depois modificar para Acesso e Perfil Enum
        );
    }
}
