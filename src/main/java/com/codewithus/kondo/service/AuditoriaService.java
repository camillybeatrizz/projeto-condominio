package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.auditoria.AuditoriaEventoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuditoriaService {

    void registrar(String tipoEvento, String entidade, UUID entidadeId, String ator, String detalhe);

    Page<AuditoriaEventoResponseDTO> listar(Pageable pageable);
}
