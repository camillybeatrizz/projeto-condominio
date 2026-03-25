package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.service.BlocoService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/blocos")
@RequiredArgsConstructor
@Tag(name = "Estrutura - Blocos", description = "Operacoes de cadastro e consulta dos blocos do condominio. A leitura tambem atende fluxos de consulta estrutural para moradores.")
@SecurityRequirement(name = "bearerAuth")
public class BlocoController {

    private final BlocoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Criar bloco", description = "Cadastra um novo bloco no condominio. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bloco criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public BlocoResponseDTO criar(@Valid @RequestBody BlocoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar blocos", description = "Retorna os blocos cadastrados. Disponivel para ADMIN, SINDICO e MORADOR, inclusive para apoiar navegacao, filtros e consultas estruturais do condominio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de blocos retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<BlocoResponseDTO> listar(@RequestParam(required = false) UUID condominioId, Pageable pageable) {
        return PageResponseDTO.from(service.listar(condominioId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Buscar bloco por ID", description = "Consulta os detalhes de um bloco especifico. Disponivel para ADMIN, SINDICO e MORADOR, inclusive quando a informacao vier vinculada a unidade, mural ou outro fluxo de consulta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloco encontrado"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bloco nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public BlocoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Atualizar bloco", description = "Atualiza as informacoes de um bloco existente. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bloco atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bloco nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public BlocoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody BlocoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Remover bloco", description = "Realiza exclusao logica de um bloco pelo identificador, preservando historico estrutural e rastreabilidade. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bloco removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bloco nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
