package com.codewithus.kondo.dto.condominio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CondominioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String nome,
        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,
        @NotBlank(message = "Telefone é obrigatório")
        String telefone,
        @NotNull(message = "Endereco é obrigatório")
        UUID enderecoId
) {
}
