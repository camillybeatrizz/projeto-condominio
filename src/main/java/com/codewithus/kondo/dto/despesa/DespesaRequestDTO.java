package com.codewithus.kondo.dto.despesa;

import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record DespesaRequestDTO(
        @NotBlank(message = "Descricao é obrigatória")
        String descricao,
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor,
        @NotNull(message = "Data é obrigatória")
        LocalDate data,
        @NotNull(message = "Categoria é obrigatória")
        CategoriaDespesaEnum categoria,
        @NotNull(message = "Condominio é obrigatório")
        UUID condominioId
) {
}
