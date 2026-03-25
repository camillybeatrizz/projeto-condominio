package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UnidadeService {

    UnidadeResponseDTO salvar(UnidadeRequestDTO dto);

    UnidadeResponseDTO buscarPorId(UUID id);

    Page<UnidadeResponseDTO> listar(UUID condominioId, UUID blocoId, String tipo, String numero, Pageable pageable);

    UnidadeResponseDTO atualizar(UUID id, UnidadeRequestDTO dto);

    void deletar(UUID id);
}
