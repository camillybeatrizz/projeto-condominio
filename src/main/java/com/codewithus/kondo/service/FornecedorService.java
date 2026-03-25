package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FornecedorService {

    FornecedorResponseDTO salvar(FornecedorRequestDTO dto);

    FornecedorResponseDTO buscarPorId(UUID id);

    Page<FornecedorResponseDTO> listar(UUID condominioId, String nome, String cnpj, Pageable pageable);

    FornecedorResponseDTO atualizar(UUID id, FornecedorRequestDTO dto);

    void deletar(UUID id);
}
