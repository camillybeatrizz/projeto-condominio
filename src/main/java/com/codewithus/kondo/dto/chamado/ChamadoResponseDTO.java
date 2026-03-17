package com.codewithus.kondo.dto.chamado;

import com.codewithus.kondo.domain.enums.StatusChamadoEnum;

import java.time.LocalDate;
import java.util.UUID;

public record ChamadoResponseDTO(
        UUID id,
        String descricao,
        StatusChamadoEnum status,
        LocalDate dataAbertura,
        UUID unidadeId
) {
}
