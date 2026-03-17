package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ChamadoService {

    ChamadoResponseDTO salvar(ChamadoRequestDTO dto);

    ChamadoResponseDTO buscarPorId(UUID id);

    List<ChamadoResponseDTO> listar();

    ChamadoResponseDTO atualizar(UUID id, ChamadoRequestDTO dto);

    void deletar(UUID id);
}
