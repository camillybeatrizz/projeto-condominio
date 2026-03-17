package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CobrancaService {

    CobrancaResponseDTO salvar(CobrancaRequestDTO dto);

    CobrancaResponseDTO buscarPorId(UUID id);

    List<CobrancaResponseDTO> listar();

    CobrancaResponseDTO atualizar(UUID id, CobrancaRequestDTO dto);

    void deletar(UUID id);
}
