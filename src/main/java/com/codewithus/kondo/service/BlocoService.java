package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BlocoService {

    BlocoResponseDTO salvar(BlocoRequestDTO dto);

    BlocoResponseDTO buscarPorId(UUID id);

    Page<BlocoResponseDTO> listar(UUID condominioId, Pageable pageable);

    BlocoResponseDTO atualizar(UUID id, BlocoRequestDTO dto);

    void deletar(UUID id);
}
