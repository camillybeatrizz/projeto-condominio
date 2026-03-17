package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;

import java.util.List;
import java.util.UUID;

public interface FornecedorService {

    FornecedorResponseDTO salvar(FornecedorRequestDTO dto);

    FornecedorResponseDTO buscarPorId(UUID id);

    List<FornecedorResponseDTO> listar();

    FornecedorResponseDTO atualizar(UUID id, FornecedorRequestDTO dto);

    void deletar(UUID id);
}
