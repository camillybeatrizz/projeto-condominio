package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.areacomum.AreaComumRequestDTO;
import com.codewithus.kondo.dto.areacomum.AreaComumResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AreaComumService {

    AreaComumResponseDTO salvar(AreaComumRequestDTO dto);

    AreaComumResponseDTO buscarPorId(UUID id);

    Page<AreaComumResponseDTO> listar(UUID condominioId, Pageable pageable);

    AreaComumResponseDTO atualizar(UUID id, AreaComumRequestDTO dto);

    void deletar(UUID id);
}
