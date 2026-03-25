package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.contrato.ContratoRequestDTO;
import com.codewithus.kondo.dto.contrato.ContratoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface ContratoService {

    ContratoResponseDTO salvar(ContratoRequestDTO dto);

    ContratoResponseDTO buscarPorId(UUID id);

    Page<ContratoResponseDTO> listar(UUID condominioId, UUID fornecedorId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    ContratoResponseDTO atualizar(UUID id, ContratoRequestDTO dto);

    void deletar(UUID id);
}
