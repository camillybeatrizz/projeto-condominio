package com.codewithus.kondo.dto.areacomum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AreaComumRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        @Schema(description = "Nome da area comum.", example = "Salão de Festas")
        String nome,
        @Size(max = 255, message = "Descricao deve ter no máximo 255 caracteres")
        @Schema(description = "Descricao opcional da area comum.", example = "Espaco para eventos e confraternizacoes.")
        String descricao,
        @NotNull(message = "Capacidade é obrigatória")
        @Positive(message = "Capacidade deve ser maior que zero")
        @Schema(description = "Capacidade máxima da area comum.", example = "60")
        Integer capacidade,
        @NotNull(message = "Condominio é obrigatório")
        @Schema(description = "Identificador do condominio ao qual a area comum pertence.", example = "660e8400-e29b-41d4-a716-446655440001")
        UUID condominioId
) {
}
