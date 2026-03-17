package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.contrato.ContratoRequestDTO;
import com.codewithus.kondo.dto.contrato.ContratoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ContratoService {

    ContratoResponseDTO salvar(ContratoRequestDTO dto);

    ContratoResponseDTO buscarPorId(UUID id);

    List<ContratoResponseDTO> listar();

    ContratoResponseDTO atualizar(UUID id, ContratoRequestDTO dto);

    void deletar(UUID id);
}
