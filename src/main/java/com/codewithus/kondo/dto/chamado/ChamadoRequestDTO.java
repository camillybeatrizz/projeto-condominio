package com.codewithus.kondo.dto.chamado;

import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ChamadoRequestDTO(
        @NotBlank(message = "Descricao é obrigatória")
        String descricao,
        @NotNull(message = "Status é obrigatório")
        StatusChamadoEnum status,
        @NotNull(message = "Data de abertura é obrigatória")
        LocalDate dataAbertura,
        @NotNull(message = "Unidade é obrigatória")
        UUID unidadeId
) {
}
