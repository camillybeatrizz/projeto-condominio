package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import com.codewithus.kondo.service.DespesaService;
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

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/despesas")
@RequiredArgsConstructor
@Tag(name = "Financeiro - Despesas", description = "Controle das despesas operacionais e administrativas do condominio.")
@SecurityRequirement(name = "bearerAuth")
public class DespesaController {

    private final DespesaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Criar despesa", description = "Cadastra uma nova despesa. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public DespesaResponseDTO criar(@Valid @RequestBody DespesaRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar despesas", description = "Retorna as despesas cadastradas. ADMIN e SINDICO podem listar as despesas no escopo permitido; MORADOR pode consultar as despesas do condominio ao qual esta vinculado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<DespesaResponseDTO> listar(
            @RequestParam(required = false) UUID condominioId,
            @RequestParam(required = false) CategoriaDespesaEnum categoria,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable
    ) {
        return PageResponseDTO.from(service.listar(condominioId, categoria, dataInicio, dataFim, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Buscar despesa por ID", description = "Consulta os detalhes de uma despesa especifica no escopo permitido. MORADOR pode acessar despesas do condominio ao qual esta vinculado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despesa encontrada"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Despesa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public DespesaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Atualizar despesa", description = "Atualiza os dados de uma despesa existente. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Despesa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public DespesaResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody DespesaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Remover despesa", description = "Realiza exclusao logica de uma despesa pelo identificador, preservando historico e rastreabilidade. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Despesa removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Despesa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
