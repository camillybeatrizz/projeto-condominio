package com.codewithus.kondo.dto.condominio;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CondominioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        @Schema(description = "Nome do condominio.", example = "Residencial Bosque das Flores")
        String nome,
        @NotBlank(message = "CNPJ é obrigatório")
        @Schema(description = "CNPJ do condominio.", example = "12.345.678/0001-99")
        String cnpj,
        @NotBlank(message = "Telefone é obrigatório")
        @Schema(description = "Telefone principal do condominio.", example = "(83) 3333-4444")
        String telefone,
        @NotNull(message = "Endereco é obrigatório")
        @Schema(description = "Identificador do endereco vinculado ao condominio.", example = "880e8400-e29b-41d4-a716-446655440003")
        UUID enderecoId
) {
}
