package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.auditoria.AuditoriaEventoResponseDTO;
import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
@Tag(name = "Auditoria", description = "Consulta dos eventos críticos registrados no sistema.")
@SecurityRequirement(name = "bearerAuth")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar eventos de auditoria", description = "Retorna os eventos críticos gravados no sistema. Disponivel apenas para ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de auditoria retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<AuditoriaEventoResponseDTO> listar(Pageable pageable) {
        return PageResponseDTO.from(auditoriaService.listar(pageable));
    }
}
