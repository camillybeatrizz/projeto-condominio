package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;
import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface DespesaService {

    DespesaResponseDTO salvar(DespesaRequestDTO dto);

    DespesaResponseDTO buscarPorId(UUID id);

    Page<DespesaResponseDTO> listar(UUID condominioId, CategoriaDespesaEnum categoria, LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    DespesaResponseDTO atualizar(UUID id, DespesaRequestDTO dto);

    void deletar(UUID id);
}
