package com.codewithus.kondo.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Perfis de acesso disponiveis na plataforma.")
public enum PerfilEnum {
    @Schema(description = "Administracao global da plataforma, usuarios e acessos.")
    ADMIN,
    @Schema(description = "Gestao estrutural e financeira do condominio.")
    SINDICO,
    @Schema(description = "Consulta dados proprios e abre chamados da propria unidade.")
    MORADOR
}
