package com.codewithus.kondo.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Schema(description = "Nome completo do usuario.", example = "Maria da Silva")
        String nome,
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Schema(description = "Email de acesso do usuario.", example = "maria.silva@kondo.com")
        String email,
        @Schema(
                description = "Identificador imutavel do usuario no provedor externo (por exemplo, o sub do Keycloak/Pinniped).",
                example = "9f41b765-2d1f-4d59-9f65-6f152c9f7c0d"
        )
        String externalId,
        @NotBlank(message = "Telefone é obrigatório")
        @Schema(description = "Telefone principal do usuario.", example = "(83) 99999-0000")
        String telefone,
        @NotNull(message = "Ativo é obrigatório")
        @Schema(description = "Indica se o usuario esta ativo.", example = "true")
        Boolean ativo
) {
}
