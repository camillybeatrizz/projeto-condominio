package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.contabancaria.ContaBancariaRequestDTO;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ContaBancariaService {

    ContaBancariaResponseDTO salvar(ContaBancariaRequestDTO dto);

    ContaBancariaResponseDTO buscarPorId(UUID id);

    List<ContaBancariaResponseDTO> listar();

    ContaBancariaResponseDTO atualizar(UUID id, ContaBancariaRequestDTO dto);

    void deletar(UUID id);
}
