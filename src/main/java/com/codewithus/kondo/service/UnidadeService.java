package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UnidadeService {

    UnidadeResponseDTO salvar(UnidadeRequestDTO dto);

    UnidadeResponseDTO buscarPorId(UUID id);

    List<UnidadeResponseDTO> listar();

    UnidadeResponseDTO atualizar(UUID id, UnidadeRequestDTO dto);

    void deletar(UUID id);
}
