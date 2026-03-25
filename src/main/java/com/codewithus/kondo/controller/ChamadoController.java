package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import com.codewithus.kondo.service.ChamadoService;
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
@RequestMapping("/chamados")
@RequiredArgsConstructor
@Tag(name = "Operacional - Chamados", description = "Abertura e acompanhamento de chamados relacionados as unidades.")
@SecurityRequirement(name = "bearerAuth")
public class ChamadoController {

    private final ChamadoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO') or @resourceOwnershipService.canCreateChamado(authentication, #dto.unidadeId())")
    @Operation(summary = "Criar chamado", description = "Abre um novo chamado para uma unidade. ADMIN e SINDICO podem criar livremente; MORADOR pode criar apenas para a propria unidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Chamado criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ChamadoResponseDTO criar(@Valid @RequestBody ChamadoRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar chamados", description = "Retorna os chamados cadastrados. ADMIN e SINDICO podem listar os chamados no escopo permitido; MORADOR lista apenas os chamados vinculados a sua propria unidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de chamados retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<ChamadoResponseDTO> listar(
            @RequestParam(required = false) UUID condominioId,
            @RequestParam(required = false) StatusChamadoEnum status,
            Pageable pageable
    ) {
        return PageResponseDTO.from(service.listar(condominioId, status, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO') or @resourceOwnershipService.canAccessChamado(authentication, #id)")
    @Operation(summary = "Buscar chamado por ID", description = "Consulta um chamado especifico. ADMIN e SINDICO acessam qualquer registro; MORADOR apenas chamados vinculados a sua unidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamado encontrado"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chamado nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ChamadoResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Atualizar chamado", description = "Atualiza os dados de um chamado existente. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamado atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chamado nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ChamadoResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody ChamadoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Remover chamado", description = "Realiza exclusao logica de um chamado pelo identificador, preservando historico e rastreabilidade. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Chamado removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Chamado nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
