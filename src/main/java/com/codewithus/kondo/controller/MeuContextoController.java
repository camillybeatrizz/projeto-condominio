package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.contexto.MeuContextoResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.service.MeuContextoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meu-contexto")
@RequiredArgsConstructor
@Tag(name = "Sessao - Meu Contexto", description = "Retorna o contexto do usuario autenticado com seus vinculos de acesso.")
@SecurityRequirement(name = "bearerAuth")
public class MeuContextoController {

    private final MeuContextoService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Buscar meu contexto", description = "Retorna os dados do usuario autenticado e os acessos vinculados a ele.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contexto retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario autenticado nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public MeuContextoResponseDTO buscar() {
        return service.buscarMeuContexto();
    }
}
