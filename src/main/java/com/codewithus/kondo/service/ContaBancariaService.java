package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.contabancaria.ContaBancariaRequestDTO;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaResponseDTO;
import com.codewithus.kondo.domain.enums.TipoContaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ContaBancariaService {

    ContaBancariaResponseDTO salvar(ContaBancariaRequestDTO dto);

    ContaBancariaResponseDTO buscarPorId(UUID id);

    Page<ContaBancariaResponseDTO> listar(UUID condominioId, TipoContaEnum tipo, String banco, Pageable pageable);

    ContaBancariaResponseDTO atualizar(UUID id, ContaBancariaRequestDTO dto);

    void deletar(UUID id);
}
