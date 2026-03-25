package com.codewithus.kondo.service;

import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaPixResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaLoteRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaLoteResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaDashboardResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResumoResponseDTO;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CobrancaService {

    CobrancaResponseDTO salvar(CobrancaRequestDTO dto);

    CobrancaResponseDTO buscarPorId(UUID id);

    CobrancaPixResponseDTO buscarDetalhesPix(UUID id);

    CobrancaResumoResponseDTO resumir(UUID condominioId);

    CobrancaDashboardResponseDTO dashboard(UUID condominioId);

    Page<CobrancaResponseDTO> listar(UUID condominioId, StatusCobrancaEnum status, String competencia, Pageable pageable);

    Page<CobrancaResponseDTO> listarInadimplentes(UUID condominioId, Pageable pageable);

    CobrancaLoteResponseDTO gerarLote(CobrancaLoteRequestDTO dto);

    CobrancaResponseDTO atualizar(UUID id, CobrancaRequestDTO dto);

    void deletar(UUID id);
}
