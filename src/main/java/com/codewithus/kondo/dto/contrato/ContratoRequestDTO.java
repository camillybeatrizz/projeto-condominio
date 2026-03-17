package com.codewithus.kondo.dto.contrato;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContratoRequestDTO(
        @NotBlank(message = "Descricao é obrigatória")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Data de inicio é obrigatória")
        LocalDate dataInicio,

        LocalDate dataFim,

        @NotNull(message = "Fornecedor é obrigatório")
        UUID fornecedorId,

        @NotNull(message = "Condominio é obrigatório")
        UUID condominioId
) {
}
