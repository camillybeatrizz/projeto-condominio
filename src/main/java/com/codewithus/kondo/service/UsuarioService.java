package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.usuario.UsuarioRequestDTO;
import com.codewithus.kondo.dto.usuario.UsuarioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    UsuarioResponseDTO salvar(UsuarioRequestDTO dto);

    UsuarioResponseDTO buscarPorId(UUID id);

    List<UsuarioResponseDTO> listar();

    UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto);

    void deletar(UUID id);
}
