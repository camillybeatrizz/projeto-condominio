package com.codewithus.kondo.dto.chamado;

import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ChamadoRequestDTO(
        @NotBlank(message = "Descricao é obrigatória")
        @Schema(description = "Descricao do problema ou solicitacao.", example = "Vazamento identificado na area de servico da unidade.")
        String descricao,
        @NotNull(message = "Status é obrigatório")
        @Schema(description = "Status atual do chamado.", example = "ABERTO")
        StatusChamadoEnum status,
        @NotNull(message = "Data de abertura é obrigatória")
        @Schema(description = "Data em que o chamado foi aberto.", example = "2026-03-18")
        LocalDate dataAbertura,
        @NotNull(message = "Unidade é obrigatória")
        @Schema(description = "Identificador da unidade relacionada ao chamado.", example = "770e8400-e29b-41d4-a716-446655440002")
        UUID unidadeId
) {
}
