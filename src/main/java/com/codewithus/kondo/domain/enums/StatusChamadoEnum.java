package com.codewithus.kondo.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status de acompanhamento de um chamado.")
public enum StatusChamadoEnum {
    @Schema(description = "Chamado aberto e aguardando atendimento.")
    ABERTO,
    @Schema(description = "Chamado em tratamento ou execucao.")
    ANDAMENTO,
    @Schema(description = "Chamado finalizado.")
    CONCLUIDO
}
