package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.usuario.UsuarioRequestDTO;
import com.codewithus.kondo.dto.usuario.UsuarioResponseDTO;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.UsuarioMapper;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        validarEmailDuplicado(dto.email(), null);

        Usuario entity = mapper.toEntity(dto);
        entity.setSenha(passwordEncoder.encode(dto.senha()));

        return mapper.toResponseDTO(usuarioRepository.save(entity));
    }


    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto) {
        Usuario entity = buscarEntidade(id);

        validarEmailDuplicado(dto.email(), id);

        mapper.updateEntity(entity, dto);
        entity.setSenha(passwordEncoder.encode(dto.senha()));

        return mapper.toResponseDTO(usuarioRepository.save(entity));
    }


    @Override
    public void deletar(UUID id) {
        usuarioRepository.delete(buscarEntidade(id));
    }

    private Usuario buscarEntidade(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }

    private void validarEmailDuplicado(String email, UUID usuarioIdAtual) {
        usuarioRepository.findByEmail(email)
                .ifPresent(usuario -> {
                    if (usuarioIdAtual == null || !usuario.getId().equals(usuarioIdAtual)) {
                        throw new ConflictException("Já existe usuário com este email");
                    }
                });
    }
}
