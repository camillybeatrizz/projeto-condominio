package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface BlocoService {

    BlocoResponseDTO salvar(BlocoRequestDTO dto);

    BlocoResponseDTO buscarPorId(UUID id);

    List<BlocoResponseDTO> listar();

    BlocoResponseDTO atualizar(UUID id, BlocoRequestDTO dto);

    void deletar(UUID id);
}
