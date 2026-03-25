package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.service.UnidadeService;
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
@RequestMapping("/unidades")
@RequiredArgsConstructor
@Tag(name = "Estrutura - Unidades", description = "Cadastro e consulta das unidades residenciais ou comerciais. Para MORADOR, a leitura se limita a propria unidade e aos dados necessarios aos fluxos relacionados.")
@SecurityRequirement(name = "bearerAuth")
public class UnidadeController {

    private final UnidadeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Criar unidade", description = "Cadastra uma nova unidade no condominio. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unidade criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public UnidadeResponseDTO criar(@Valid @RequestBody UnidadeRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar unidades", description = "Retorna as unidades cadastradas. ADMIN e SINDICO podem listar as unidades no escopo permitido; MORADOR lista apenas a propria unidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de unidades retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<UnidadeResponseDTO> listar(
            @RequestParam(required = false) UUID condominioId,
            @RequestParam(required = false) UUID blocoId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String numero,
            Pageable pageable
    ) {
        return PageResponseDTO.from(service.listar(condominioId, blocoId, tipo, numero, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO') or @resourceOwnershipService.canAccessUnidade(authentication, #id)")
    @Operation(summary = "Buscar unidade por ID", description = "Consulta uma unidade especifica. ADMIN e SINDICO acessam qualquer registro no escopo permitido; MORADOR acessa apenas a propria unidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade encontrada"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Unidade nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public UnidadeResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Atualizar unidade", description = "Atualiza os dados de uma unidade existente. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unidade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Unidade nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public UnidadeResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody UnidadeRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Remover unidade", description = "Realiza exclusao logica de uma unidade pelo identificador, preservando historico estrutural e rastreabilidade. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unidade removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Unidade nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
