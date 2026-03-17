package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.pagamento.PagamentoRequestDTO;
import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PagamentoService {

    PagamentoResponseDTO salvar(PagamentoRequestDTO dto);

    PagamentoResponseDTO buscarPorId(UUID id);

    List<PagamentoResponseDTO> listar();

    PagamentoResponseDTO atualizar(UUID id, PagamentoRequestDTO dto);

    void deletar(UUID id);
}
