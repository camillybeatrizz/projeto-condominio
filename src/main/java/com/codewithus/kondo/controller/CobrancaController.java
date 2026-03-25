package com.codewithus.kondo.controller;

import com.codewithus.kondo.dto.common.PageResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaLoteRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaLoteResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaPixResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaDashboardResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResumoResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;
import com.codewithus.kondo.dto.error.ErrorResponseDTO;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.service.CobrancaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/cobrancas")
@RequiredArgsConstructor
@Tag(name = "Financeiro - Cobrancas", description = "Controle de cobrancas emitidas para as unidades do condominio.")
@SecurityRequirement(name = "bearerAuth")
public class CobrancaController {

    private final CobrancaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(
            summary = "Criar cobranca",
            description = """
                    Gera uma nova cobranca para uma unidade.

                    Regras principais:
                    - disponivel para ADMIN e SINDICO
                    - a cobranca nasce com status operacional do MVP, como ABERTA
                    - ao criar, o backend pode integrar com o gateway e preencher referencia externa, URL de pagamento e dados Pix
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cobranca criada com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = CobrancaResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "cobrancaCriada",
                                    value = """
                                            {
                                              "id": "aa0e8400-e29b-41d4-a716-446655440005",
                                              "valor": 450.00,
                                              "vencimento": "2026-04-10",
                                              "status": "ABERTA",
                                              "competencia": "2026-04",
                                              "referenciaExterna": "pay_000123456",
                                              "urlPagamentoExterno": "https://sandbox.asaas.com/i/pay_000123456",
                                              "unidadeId": "770e8400-e29b-41d4-a716-446655440002",
                                              "createdAt": "2026-03-25T10:00:00",
                                              "updatedAt": "2026-03-25T10:00:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaResponseDTO criar(@Valid @RequestBody CobrancaRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar cobrancas", description = "Retorna as cobrancas cadastradas. ADMIN e SINDICO podem listar as cobrancas no escopo permitido; MORADOR lista apenas as proprias cobrancas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cobrancas retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<CobrancaResponseDTO> listar(
            @RequestParam(required = false) UUID condominioId,
            @RequestParam(required = false) StatusCobrancaEnum status,
            @RequestParam(required = false) String competencia,
            Pageable pageable
    ) {
        return PageResponseDTO.from(service.listar(condominioId, status, competencia, pageable));
    }

    @GetMapping("/resumo")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(
            summary = "Resumo financeiro de cobrancas",
            description = """
                    Retorna indicadores agregados das cobrancas no escopo do usuario autenticado.

                    Uso recomendado:
                    - cards de dashboard
                    - cabecalho da tela financeira
                    - visao resumida de morador ou sindico

                    Permissoes:
                    - ADMIN pode consultar qualquer escopo
                    - SINDICO pode consultar apenas condominios aos quais tem acesso
                    - MORADOR recebe apenas dados das proprias cobrancas
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumo retornado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = CobrancaResumoResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "resumoFinanceiro",
                                    value = """
                                            {
                                              "totalCobrancas": 12,
                                              "totalAbertas": 5,
                                              "totalPagas": 4,
                                              "totalInadimplentes": 3,
                                              "valorTotal": 5400.00,
                                              "valorAberto": 2100.00,
                                              "valorPago": 2500.00,
                                              "valorInadimplente": 800.00
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaResumoResponseDTO resumir(
            @Parameter(description = "Filtro opcional por condominio. Obrigatorio apenas quando o frontend quiser resumir um condominio especifico no contexto de ADMIN ou SINDICO.", example = "660e8400-e29b-41d4-a716-446655440001")
            @RequestParam(required = false) UUID condominioId
    ) {
        return service.resumir(condominioId);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(
            summary = "Dashboard financeiro de cobrancas",
            description = """
                    Retorna uma resposta consolidada para telas de dashboard financeiro.

                    A resposta combina:
                    - resumo agregado de cobrancas
                    - inadimplentes recentes
                    - pagamentos recentes

                    Uso recomendado:
                    - tela inicial financeira do sindico
                    - painel simplificado do morador
                    - dashboard administrativo com filtro por condominio
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard retornado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = CobrancaDashboardResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "dashboardFinanceiro",
                                    value = """
                                            {
                                              "resumo": {
                                                "totalCobrancas": 12,
                                                "totalAbertas": 5,
                                                "totalPagas": 4,
                                                "totalInadimplentes": 3,
                                                "valorTotal": 5400.00,
                                                "valorAberto": 2100.00,
                                                "valorPago": 2500.00,
                                                "valorInadimplente": 800.00
                                              },
                                              "inadimplentesRecentes": [
                                                {
                                                  "id": "aa0e8400-e29b-41d4-a716-446655440005",
                                                  "valor": 450.00,
                                                  "vencimento": "2026-04-10",
                                                  "status": "VENCIDA",
                                                  "competencia": "2026-04",
                                                  "referenciaExterna": "pay_000123456",
                                                  "urlPagamentoExterno": "https://sandbox.asaas.com/i/pay_000123456",
                                                  "unidadeId": "770e8400-e29b-41d4-a716-446655440002",
                                                  "createdAt": "2026-03-25T10:00:00",
                                                  "updatedAt": "2026-03-25T10:00:00"
                                                }
                                              ],
                                              "pagamentosRecentes": [
                                                {
                                                  "id": "220e8400-e29b-41d4-a716-446655440012",
                                                  "valor": 450.00,
                                                  "dataPagamento": "2026-04-08",
                                                  "forma": "PIX",
                                                  "transactionId": "PIX-20260408-000123",
                                                  "cobrancaId": "aa0e8400-e29b-41d4-a716-446655440005",
                                                  "createdAt": "2026-03-18T10:30:00",
                                                  "updatedAt": "2026-03-18T10:30:00"
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaDashboardResponseDTO dashboard(
            @Parameter(description = "Filtro opcional por condominio. Quando omitido, o backend resume todo o escopo permitido ao usuario autenticado.", example = "660e8400-e29b-41d4-a716-446655440001")
            @RequestParam(required = false) UUID condominioId
    ) {
        return service.dashboard(condominioId);
    }

    @GetMapping("/inadimplentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO', 'MORADOR')")
    @Operation(summary = "Listar inadimplentes", description = "Retorna as cobrancas vencidas e nao pagas. Disponivel para ADMIN, SINDICO e MORADOR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inadimplentes retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public PageResponseDTO<CobrancaResponseDTO> listarInadimplentes(
            @RequestParam(required = false) UUID condominioId,
            Pageable pageable
    ) {
        return PageResponseDTO.from(service.listarInadimplentes(condominioId, pageable));
    }

    @PostMapping("/gerar-lote")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Gerar cobrancas em lote", description = "Gera cobrancas simples para todas as unidades de um condominio em uma competencia informada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lote gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaLoteResponseDTO gerarLote(@Valid @RequestBody CobrancaLoteRequestDTO dto) {
        return service.gerarLote(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO') or @resourceOwnershipService.canAccessCobranca(authentication, #id)")
    @Operation(summary = "Buscar cobranca por ID", description = "Consulta uma cobranca especifica. ADMIN e SINDICO acessam qualquer registro; MORADOR apenas cobrancas vinculadas a sua unidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cobranca encontrada"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cobranca nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaResponseDTO buscar(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/{id}/pix")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO') or @resourceOwnershipService.canAccessCobranca(authentication, #id)")
    @Operation(
            summary = "Buscar detalhes Pix da cobranca",
            description = """
                    Retorna os dados necessarios para a experiencia de pagamento Pix da cobranca.

                    A resposta foi desenhada para o frontend exibir:
                    - botao de abrir link externo
                    - botao de copiar codigo Pix
                    - QR Code Pix em imagem
                    - informacao de expiracao

                    Permissoes:
                    - ADMIN e SINDICO podem consultar conforme escopo
                    - MORADOR pode consultar apenas a propria cobranca
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detalhes Pix retornados com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = CobrancaPixResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "detalhesPix",
                                    value = """
                                            {
                                              "cobrancaId": "aa0e8400-e29b-41d4-a716-446655440005",
                                              "referenciaExterna": "pay_000123456",
                                              "urlPagamentoExterno": "https://sandbox.asaas.com/i/pay_000123456",
                                              "pixCopiaCola": "00020101021226890014br.gov.bcb.pix2567pix-h.example.com/qr/v2/cob/abc123520400005303986540410.005802BR5913KONDO TESTE6008BRASILIA62070503***6304ABCD",
                                              "pixQrCodeBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
                                              "pixExpiracao": "2026-04-10T23:59:59"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cobranca nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaPixResponseDTO buscarPix(@PathVariable UUID id) {
        return service.buscarDetalhesPix(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Atualizar cobranca", description = "Atualiza os dados de uma cobranca existente. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cobranca atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cobranca nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public CobrancaResponseDTO atualizar(@PathVariable UUID id, @Valid @RequestBody CobrancaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'SINDICO')")
    @Operation(summary = "Remover cobranca", description = "Realiza exclusao logica de uma cobranca pelo identificador, preservando historico financeiro e rastreabilidade. Disponivel para ADMIN e SINDICO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cobranca removida com sucesso"),
            @ApiResponse(responseCode = "401", description = "Nao autenticado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cobranca nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}
