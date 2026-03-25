package com.codewithus.kondo.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EnderecoRequestDTO(
        @NotBlank(message = "Logradouro é obrigatório")
        @Schema(description = "Logradouro do endereco.", example = "Rua das Palmeiras")
        String logradouro,
        @NotBlank(message = "Numero é obrigatório")
        @Schema(description = "Numero do endereco.", example = "120")
        String numero,
        @Schema(description = "Complemento do endereco, quando existir.", example = "Ao lado da praca central")
        String complemento,
        @NotBlank(message = "Bairro é obrigatório")
        @Schema(description = "Bairro do endereco.", example = "Centro")
        String bairro,
        @NotBlank(message = "Cidade é obrigatória")
        @Schema(description = "Cidade do endereco.", example = "Joao Pessoa")
        String cidade,
        @NotBlank(message = "Estado é obrigatório")
        @Schema(description = "UF do endereco.", example = "PB")
        String estado,
        @NotBlank(message = "CEP é obrigatório")
        @Schema(description = "CEP do endereco.", example = "58000-000")
        String cep
) {
}
