package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.endereco.EnderecoRequestDTO;
import com.codewithus.kondo.dto.endereco.EnderecoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface EnderecoService {

    EnderecoResponseDTO salvar(EnderecoRequestDTO dto);

    EnderecoResponseDTO buscarPorId(UUID id);

    List<EnderecoResponseDTO> listar();

    EnderecoResponseDTO atualizar(UUID id, EnderecoRequestDTO dto);

    void deletar(UUID id);
}
