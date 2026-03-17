package com.codewithus.kondo.dto.fornecedor;

import jakarta.validation.constraints.NotBlank;

public record FornecedorRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,
        @NotBlank(message = "Telefone é obrigatório")
        String telefone
) {
}
