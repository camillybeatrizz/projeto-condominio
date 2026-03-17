package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.acesso.AcessoRequestDTO;
import com.codewithus.kondo.dto.acesso.AcessoResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.AcessoMapper;
import com.codewithus.kondo.repository.AcessoRepository;
import com.codewithus.kondo.repository.UsuarioRepository;
import com.codewithus.kondo.service.AcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcessoServiceImpl implements AcessoService {

    private final AcessoRepository acessoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AcessoMapper mapper;

    @Override
    public AcessoResponseDTO salvar(AcessoRequestDTO dto) {
        Usuario usuario = buscarUsuario(dto.usuarioId());
        Acesso entity = mapper.toEntity(dto, usuario);
        return mapper.toResponseDTO(acessoRepository.save(entity));
    }

    @Override
    public AcessoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<AcessoResponseDTO> listar() {
        return acessoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public AcessoResponseDTO atualizar(UUID id, AcessoRequestDTO dto) {
        Acesso entity = buscarEntidade(id);
        Usuario usuario = buscarUsuario(dto.usuarioId());
        mapper.updateEntity(entity, dto, usuario);
        return mapper.toResponseDTO(acessoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        acessoRepository.delete(buscarEntidade(id));
    }

    private Acesso buscarEntidade(UUID id) {
        return acessoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acesso não encontrado"));
    }

    private Usuario buscarUsuario(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }
}
