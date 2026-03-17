package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.acesso.AcessoRequestDTO;
import com.codewithus.kondo.dto.acesso.AcessoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AcessoService {

    AcessoResponseDTO salvar(AcessoRequestDTO dto);

    AcessoResponseDTO buscarPorId(UUID id);

    List<AcessoResponseDTO> listar();

    AcessoResponseDTO atualizar(UUID id, AcessoRequestDTO dto);

    void deletar(UUID id);
}
