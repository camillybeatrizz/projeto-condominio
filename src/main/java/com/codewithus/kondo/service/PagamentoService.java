package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.pagamento.PagamentoRequestDTO;
import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface PagamentoService {

    PagamentoResponseDTO salvar(PagamentoRequestDTO dto);

    PagamentoResponseDTO buscarPorId(UUID id);

    Page<PagamentoResponseDTO> listar(UUID condominioId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    PagamentoResponseDTO atualizar(UUID id, PagamentoRequestDTO dto);

    void deletar(UUID id);
}
