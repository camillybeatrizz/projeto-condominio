package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.condominio.CondominioRequestDTO;
import com.codewithus.kondo.dto.condominio.CondominioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CondominioService {

    CondominioResponseDTO salvar(CondominioRequestDTO dto);

    CondominioResponseDTO buscarPorId(UUID id);

    List<CondominioResponseDTO> listar();

    CondominioResponseDTO atualizar(UUID id, CondominioRequestDTO dto);

    void deletar(UUID id);
}
