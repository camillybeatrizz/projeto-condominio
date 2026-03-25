package com.codewithus.kondo.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categoria usada para classificar despesas do condominio.")
public enum CategoriaDespesaEnum {
    @Schema(description = "Despesas de reparo, conservacao ou suporte tecnico.")
    MANUTENCAO,
    @Schema(description = "Despesas com materiais e servicos de limpeza.")
    LIMPEZA,
    @Schema(description = "Despesas relacionadas a vigilancia e controle de acesso.")
    SEGURANCA,
    @Schema(description = "Demais despesas que nao se enquadram nas categorias anteriores.")
    OUTROS
}
