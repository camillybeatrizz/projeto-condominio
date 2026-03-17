package com.codewithus.kondo.dto.condominio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CondominioUpdateDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotNull(message = "Endereco é obrigatório")
        UUID enderecoId
) {}
