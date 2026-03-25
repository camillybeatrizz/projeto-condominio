package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.condominio.CondominioRequestDTO;
import com.codewithus.kondo.dto.condominio.CondominioResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CondominioService {

    CondominioResponseDTO salvar(CondominioRequestDTO dto);

    CondominioResponseDTO buscarPorId(UUID id);

    Page<CondominioResponseDTO> listar(Pageable pageable);

    CondominioResponseDTO atualizar(UUID id, CondominioRequestDTO dto);

    void deletar(UUID id);
}
