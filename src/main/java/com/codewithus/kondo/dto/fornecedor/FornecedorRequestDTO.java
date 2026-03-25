package com.codewithus.kondo.dto.fornecedor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record FornecedorRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome empresarial do fornecedor.", example = "Elevadores Paraiba LTDA")
        String nome,
        @NotBlank(message = "CNPJ é obrigatório")
        @Schema(description = "CNPJ do fornecedor.", example = "98.765.432/0001-10")
        String cnpj,
        @NotBlank(message = "Telefone é obrigatório")
        @Schema(description = "Telefone de contato do fornecedor.", example = "(83) 98888-7777")
        String telefone
) {
}
