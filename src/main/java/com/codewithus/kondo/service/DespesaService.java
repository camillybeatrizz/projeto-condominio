package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DespesaService {

    DespesaResponseDTO salvar(DespesaRequestDTO dto);

    DespesaResponseDTO buscarPorId(UUID id);

    List<DespesaResponseDTO> listar();

    DespesaResponseDTO atualizar(UUID id, DespesaRequestDTO dto);

    void deletar(UUID id);
}
