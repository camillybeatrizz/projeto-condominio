package com.codewithus.kondo.security;

import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioIdentityLinkService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<Usuario> linkExternalIdIfEligible(Usuario usuario, String subject) {
        String externalIdAtual = usuario.getExternalId();

        if (externalIdAtual == null || externalIdAtual.isBlank()) {
            usuario.setExternalId(subject);
            return Optional.of(usuarioRepository.saveAndFlush(usuario));
        }

        if (externalIdAtual.equals(subject)) {
            return Optional.of(usuario);
        }

        return Optional.empty();
    }
}
