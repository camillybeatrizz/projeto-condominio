package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;
import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChamadoService {

    ChamadoResponseDTO salvar(ChamadoRequestDTO dto);

    ChamadoResponseDTO buscarPorId(UUID id);

    Page<ChamadoResponseDTO> listar(UUID condominioId, StatusChamadoEnum status, Pageable pageable);

    ChamadoResponseDTO atualizar(UUID id, ChamadoRequestDTO dto);

    void deletar(UUID id);
}
