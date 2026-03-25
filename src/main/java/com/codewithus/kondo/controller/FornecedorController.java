package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.service.FornecedorService;
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
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
@Tag(name = "Administrativo - Fornecedores", description = "Cadastro e consulta dos fornecedores contratados pelo condominio.")
@SecurityRequirement(name = "bearerAuth")
public class FornecedorController {

    private final FornecedorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Criar fornecedor", description = "Cadastra um novo fornecedor. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public FornecedorResponseDTO criar(@Valid @RequestBody FornecedorRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar fornecedores", description = "Retorna os fornecedores cadastrados. ADMIN e SINDICO podem listar os fornecedores no escopo permitido; MORADOR pode consultar os fornecedores vinculados ao condominio ao qual esta associado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<FornecedorResponseDTO> listar(
            @RequestParam(required = false) UUID condominioId,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cnpj,
            Pageable pageable
    ) {
        return PageResponseDTO.from(service.listar(condominioId, nome, cnpj, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Buscar fornecedor por ID", description = "Consulta os detalhes de um fornecedor especifico no escopo permitido, incluindo o contexto do condominio do morador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public FornecedorResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Atualizar fornecedor", description = "Atualiza os dados de um fornecedor existente. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public FornecedorResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody FornecedorRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Remover fornecedor", description = "Realiza exclusao logica de um fornecedor pelo identificador, preservando historico e rastreabilidade. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornecedor removido com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
