package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.contexto.MeuAcessoResponseDTO;
import com.codewithus.kondo.dto.contexto.MeuContextoResponseDTO;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.security.CurrentUserResolver;
import com.codewithus.kondo.service.MeuContextoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeuContextoServiceImpl implements MeuContextoService {

    private final CurrentUserResolver currentUserResolver;
    private final AcessoRepository acessoRepository;

    @Override
    public MeuContextoResponseDTO buscarMeuContexto() {
        Usuario usuario = currentUserResolver.getRequiredUsuario();

        List<MeuAcessoResponseDTO> acessos = acessoRepository.findAllByUsuario_Id(usuario.getId()).stream()
                .map(this::toMeuAcessoResponseDTO)
                .toList();

        return new MeuContextoResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getAtivo(),
                acessos
        );
    }

    private MeuAcessoResponseDTO toMeuAcessoResponseDTO(Acesso acesso) {
        return new MeuAcessoResponseDTO(
                acesso.getId(),
                acesso.getPerfil(),
                acesso.getCondominio() != null ? acesso.getCondominio().getId() : null,
                acesso.getCondominio() != null ? acesso.getCondominio().getNome() : null,
                acesso.getUnidade() != null ? acesso.getUnidade().getId() : null,
                acesso.getUnidade() != null ? acesso.getUnidade().getNumero() : null
        );
    }
}
