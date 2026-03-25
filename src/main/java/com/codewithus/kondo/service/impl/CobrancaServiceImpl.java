package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.dto.cobranca.CobrancaLoteRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaLoteResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaDashboardResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaPixResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResumoResponseDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;
import com.codewithus.kondo.dto.integration.AsaasCobrancaResult;
import com.codewithus.kondo.dto.integration.AsaasPixQrCodeResult;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.CobrancaMapper;
import com.codewithus.kondo.mapper.PagamentoMapper;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.repository.specification.CobrancaSpecifications;
import com.codewithus.kondo.repository.specification.PagamentoSpecifications;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.security.CurrentUserResolver;
import com.codewithus.kondo.service.AsaasCobrancaGateway;
import com.codewithus.kondo.service.CobrancaService;
import com.codewithus.kondo.dto.integration.AsaasCobrancaCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CobrancaServiceImpl implements CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final UnidadeRepository unidadeRepository;
    private final PagamentoRepository pagamentoRepository;
    private final CobrancaMapper mapper;
    private final PagamentoMapper pagamentoMapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CurrentUserResolver currentUserResolver;
    private final CondominioScopeService condominioScopeService;
    private final AsaasCobrancaGateway asaasCobrancaGateway;

    @Override
    public CobrancaResponseDTO salvar(CobrancaRequestDTO dto) {

        validarRegraDeCobranca(dto);

        Unidade unidade = buscarUnidade(dto.unidadeId());
        condominioScopeService.assertCanAccessCondominio(unidade.getBloco().getCondominio().getId());
        validarDuplicidadeDeCobrancaAtiva(unidade.getId(), dto.competencia(), null);
        validarReferenciaExternaGlobal(dto.referenciaExterna(), null);
        Cobranca entity = mapper.toEntity(dto, unidade);
        entity = cobrancaRepository.save(entity);
        preencherReferenciaExternaSeNecessario(entity);
        return mapper.toResponseDTO(entity);
    }

    @Override
    public CobrancaResponseDTO buscarPorId(UUID id) {
        Cobranca cobranca = buscarEntidade(id);
        if (!authenticatedUserFacade.isMorador()) {
            condominioScopeService.assertCanAccessCondominio(cobranca.getUnidade().getBloco().getCondominio().getId());
        }
        return mapper.toResponseDTO(cobranca);
    }

    @Override
    public CobrancaPixResponseDTO buscarDetalhesPix(UUID id) {
        Cobranca cobranca = buscarEntidade(id);
        if (!authenticatedUserFacade.isMorador()) {
            condominioScopeService.assertCanAccessCondominio(cobranca.getUnidade().getBloco().getCondominio().getId());
        }

        preencherDadosPixSeNecessario(cobranca);

        return new CobrancaPixResponseDTO(
                cobranca.getId(),
                cobranca.getReferenciaExterna(),
                cobranca.getUrlPagamentoExterno(),
                cobranca.getPixCopiaCola(),
                cobranca.getPixQrCodeBase64(),
                cobranca.getPixExpiracao()
        );
    }

    @Override
    public CobrancaResumoResponseDTO resumir(UUID condominioId) {
        Specification<Cobranca> specification = montarEspecificacaoBase(condominioId);
        List<Cobranca> cobrancas = cobrancaRepository.findAll(specification);

        long totalCobrancas = cobrancas.size();
        long totalAbertas = cobrancas.stream()
                .filter(cobranca -> cobranca.getStatus() == StatusCobrancaEnum.ABERTA)
                .count();
        long totalPagas = cobrancas.stream()
                .filter(cobranca -> cobranca.getStatus() == StatusCobrancaEnum.PAGA)
                .count();
        long totalInadimplentes = cobrancas.stream()
                .filter(this::isInadimplente)
                .count();

        BigDecimal valorTotal = somarValores(cobrancas);
        BigDecimal valorAberto = somarValores(cobrancas.stream()
                .filter(cobranca -> cobranca.getStatus() == StatusCobrancaEnum.ABERTA)
                .toList());
        BigDecimal valorPago = somarValores(cobrancas.stream()
                .filter(cobranca -> cobranca.getStatus() == StatusCobrancaEnum.PAGA)
                .toList());
        BigDecimal valorInadimplente = somarValores(cobrancas.stream()
                .filter(this::isInadimplente)
                .toList());

        return new CobrancaResumoResponseDTO(
                totalCobrancas,
                totalAbertas,
                totalPagas,
                totalInadimplentes,
                valorTotal,
                valorAberto,
                valorPago,
                valorInadimplente
        );
    }

    @Override
    public CobrancaDashboardResponseDTO dashboard(UUID condominioId) {
        CobrancaResumoResponseDTO resumo = resumir(condominioId);

        PageRequest limiteInadimplentes = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "vencimento"));
        List<CobrancaResponseDTO> inadimplentesRecentes = cobrancaRepository.findAll(
                        montarEspecificacaoBase(condominioId).and(CobrancaSpecifications.isInadimplente(java.time.LocalDate.now())),
                        limiteInadimplentes
                )
                .map(mapper::toResponseDTO)
                .getContent();

        PageRequest limitePagamentos = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "dataPagamento").and(Sort.by(Sort.Direction.DESC, "createdAt")));
        List<com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO> pagamentosRecentes = pagamentoRepository.findAll(
                        montarEspecificacaoPagamentoBase(condominioId),
                        limitePagamentos
                )
                .map(pagamentoMapper::toResponseDTO)
                .getContent();

        return new CobrancaDashboardResponseDTO(
                resumo,
                inadimplentesRecentes,
                pagamentosRecentes
        );
    }

    @Override
    public Page<CobrancaResponseDTO> listar(UUID condominioId, StatusCobrancaEnum status, String competencia, Pageable pageable) {
        Specification<Cobranca> specification = montarEspecificacaoBase(condominioId);
        if (authenticatedUserFacade.isMorador() && currentUserResolver.findCurrentUsuarioId().isEmpty()) {
            return Page.empty(pageable);
        }

        if (status != null) {
            specification = specification.and(CobrancaSpecifications.hasStatus(status));
        }

        if (competencia != null && !competencia.isBlank()) {
            specification = specification.and(CobrancaSpecifications.hasCompetencia(competencia.trim()));
        }

        Page<Cobranca> cobrancas = cobrancaRepository.findAll(specification, pageable);

        return cobrancas.map(mapper::toResponseDTO);
    }

    @Override
    public Page<CobrancaResponseDTO> listarInadimplentes(UUID condominioId, Pageable pageable) {
        Specification<Cobranca> specification = montarEspecificacaoBase(condominioId);
        if (authenticatedUserFacade.isMorador() && currentUserResolver.findCurrentUsuarioId().isEmpty()) {
            return Page.empty(pageable);
        }

        specification = specification.and(CobrancaSpecifications.isInadimplente(java.time.LocalDate.now()));

        Page<Cobranca> cobrancas = cobrancaRepository.findAll(specification, pageable);
        return cobrancas.map(mapper::toResponseDTO);
    }

    @Override
    public CobrancaLoteResponseDTO gerarLote(CobrancaLoteRequestDTO dto) {
        if (dto.competencia() == null || dto.competencia().isBlank()) {
            throw new BusinessException("Competência é obrigatória");
        }

        if (dto.valor() == null || dto.valor().signum() <= 0) {
            throw new BusinessException("Valor deve ser positivo");
        }

        if (dto.vencimento() == null) {
            throw new BusinessException("Vencimento é obrigatório");
        }

        condominioScopeService.assertCanAccessCondominio(dto.condominioId());

        List<Unidade> unidades = unidadeRepository.findAllByBloco_Condominio_Id(dto.condominioId());
        List<CobrancaResponseDTO> cobrancasCriadas = new ArrayList<>();

        for (Unidade unidade : unidades) {
            if (cobrancaRepository.existsByUnidade_IdAndCompetenciaIgnoreCaseAndDeletedAtIsNull(unidade.getId(), dto.competencia())) {
                continue;
            }

            Cobranca cobranca = new Cobranca();
            cobranca.setValor(dto.valor());
            cobranca.setVencimento(dto.vencimento());
            cobranca.setStatus(StatusCobrancaEnum.ABERTA);
            cobranca.setCompetencia(dto.competencia());
            cobranca.setUnidade(unidade);
            cobranca = cobrancaRepository.save(cobranca);
            preencherReferenciaExternaSeNecessario(cobranca);
            cobrancasCriadas.add(mapper.toResponseDTO(cobranca));
        }

        return new CobrancaLoteResponseDTO(
                unidades.size(),
                cobrancasCriadas.size(),
                unidades.size() - cobrancasCriadas.size(),
                cobrancasCriadas
        );
    }

    @Override
    public CobrancaResponseDTO atualizar(UUID id, CobrancaRequestDTO dto) {

        validarRegraDeCobranca(dto);

        Cobranca entity = buscarEntidade(id);
        Unidade unidade = buscarUnidade(dto.unidadeId());
        condominioScopeService.assertCanAccessCondominio(entity.getUnidade().getBloco().getCondominio().getId());
        condominioScopeService.assertCanAccessCondominio(unidade.getBloco().getCondominio().getId());
        validarDuplicidadeDeCobrancaAtiva(unidade.getId(), dto.competencia(), id);
        validarReferenciaExternaGlobal(dto.referenciaExterna(), id);
        mapper.updateEntity(entity, dto, unidade);
        entity = cobrancaRepository.save(entity);
        preencherReferenciaExternaSeNecessario(entity);
        return mapper.toResponseDTO(entity);
    }

    @Override
    public void deletar(UUID id) {
        Cobranca cobranca = buscarEntidade(id);
        condominioScopeService.assertCanAccessCondominio(cobranca.getUnidade().getBloco().getCondominio().getId());
        cobranca.setDeletedAt(java.time.LocalDateTime.now());
        cobranca.setDeletedBy(resolveAuditActor());
        cobrancaRepository.save(cobranca);
    }

    private Cobranca buscarEntidade(UUID id) {
        return cobrancaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança não encontrada"));
    }

    private Unidade buscarUnidade(UUID id) {
        return unidadeRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    private void validarRegraDeCobranca(CobrancaRequestDTO dto) {
        if (dto.status() == StatusCobrancaEnum.PAGA) {
            throw new BusinessException("Não é permitido criar ou atualizar cobrança diretamente com status PAGA");
        }

        if (dto.competencia() == null || dto.competencia().isBlank()) {
            throw new BusinessException("Competência é obrigatória");
        }
    }

    private void validarDuplicidadeDeCobrancaAtiva(UUID unidadeId, String competencia, UUID cobrancaAtualId) {
        if (unidadeId == null || competencia == null || competencia.isBlank()) {
            return;
        }

        List<Cobranca> cobrancasAtivas = cobrancaRepository.findAll(
                CobrancaSpecifications.isNotDeleted()
                        .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("unidade").get("id"), unidadeId))
                        .and(CobrancaSpecifications.hasCompetencia(competencia))
        );

        boolean existeDuplicidade = cobrancasAtivas.stream()
                .anyMatch(cobranca -> cobrancaAtualId == null || !cobranca.getId().equals(cobrancaAtualId));

        if (existeDuplicidade) {
            throw new ConflictException("Já existe cobrança ativa com esta competência para a unidade informada");
        }
    }

    private void validarReferenciaExternaGlobal(String referenciaExterna, UUID cobrancaAtualId) {
        if (referenciaExterna == null || referenciaExterna.isBlank()) {
            return;
        }

        cobrancaRepository.findByReferenciaExterna(referenciaExterna)
                .ifPresent(cobranca -> {
                    if (cobrancaAtualId == null || !cobranca.getId().equals(cobrancaAtualId)) {
                        throw new ConflictException("Já existe cobrança com esta referência externa");
                    }
                });
    }

    private void preencherReferenciaExternaSeNecessario(Cobranca cobranca) {
        if (cobranca.getReferenciaExterna() != null && !cobranca.getReferenciaExterna().isBlank()) {
            return;
        }

        AsaasCobrancaResult resultado = asaasCobrancaGateway.criarCobranca(new AsaasCobrancaCommand(
                cobranca.getId(),
                cobranca.getUnidade() != null ? cobranca.getUnidade().getId() : null,
                obterUsuarioId(cobranca),
                obterPagadorNome(cobranca),
                obterPagadorEmail(cobranca),
                obterPagadorTelefone(cobranca),
                cobranca.getValor(),
                cobranca.getVencimento(),
                cobranca.getCompetencia()
        ));
        cobranca.setReferenciaExterna(resultado.referenciaExterna());
        cobranca.setUrlPagamentoExterno(resultado.urlPagamentoExterno());
        preencherDadosPixSeNecessario(cobranca);
        cobrancaRepository.save(cobranca);
    }

    private void preencherDadosPixSeNecessario(Cobranca cobranca) {
        if (cobranca.getReferenciaExterna() == null || cobranca.getReferenciaExterna().isBlank()) {
            return;
        }

        if (cobranca.getPixCopiaCola() != null && !cobranca.getPixCopiaCola().isBlank()
                && cobranca.getPixQrCodeBase64() != null && !cobranca.getPixQrCodeBase64().isBlank()
                && cobranca.getPixExpiracao() != null) {
            return;
        }

        AsaasPixQrCodeResult qrCode = asaasCobrancaGateway.consultarPixQrCode(cobranca.getReferenciaExterna());
        cobranca.setPixCopiaCola(qrCode.payload());
        cobranca.setPixQrCodeBase64(qrCode.encodedImage());
        cobranca.setPixExpiracao(qrCode.expirationDate());
        cobrancaRepository.save(cobranca);
    }

    private UUID obterUsuarioId(Cobranca cobranca) {
        Usuario morador = obterMorador(cobranca);
        return morador != null ? morador.getId() : null;
    }

    private String obterPagadorNome(Cobranca cobranca) {
        Usuario morador = obterMorador(cobranca);
        return morador != null ? morador.getNome() : null;
    }

    private String obterPagadorEmail(Cobranca cobranca) {
        Usuario morador = obterMorador(cobranca);
        return morador != null ? morador.getEmail() : null;
    }

    private String obterPagadorTelefone(Cobranca cobranca) {
        Usuario morador = obterMorador(cobranca);
        return morador != null ? morador.getTelefone() : null;
    }

    private Usuario obterMorador(Cobranca cobranca) {
        return cobranca.getUnidade() != null ? cobranca.getUnidade().getMorador() : null;
    }

    private Specification<Cobranca> montarEspecificacaoBase(UUID condominioId) {
        if (condominioId != null && authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Specification<Cobranca> specification = Specification.allOf(CobrancaSpecifications.isNotDeleted());

        if (authenticatedUserFacade.isMorador()) {
            if (currentUserResolver.findCurrentUsuarioId().isEmpty()) {
                return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.disjunction());
            }
            specification = specification.and(CobrancaSpecifications.hasMoradorId(currentUserResolver.getRequiredUsuarioId()));
        }

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin() && !authenticatedUserFacade.isMorador()) {
            specification = specification.and(CobrancaSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(CobrancaSpecifications.hasCondominioId(condominioId));
        }

        return specification;
    }

    private Specification<com.codewithus.kondo.domain.entity.Pagamento> montarEspecificacaoPagamentoBase(UUID condominioId) {
        if (condominioId != null && authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()) {
            condominioScopeService.assertCanAccessCondominio(condominioId);
        }

        Specification<com.codewithus.kondo.domain.entity.Pagamento> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (authenticatedUserFacade.isMorador()) {
            if (currentUserResolver.findCurrentUsuarioId().isEmpty()) {
                return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.disjunction());
            }
            specification = specification.and(PagamentoSpecifications.hasMoradorId(currentUserResolver.getRequiredUsuarioId()));
        }

        if (authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin() && !authenticatedUserFacade.isMorador()) {
            specification = specification.and(PagamentoSpecifications.hasCondominioIdIn(condominioScopeService.getCondominioIdsPermitidos()));
        }

        if (condominioId != null) {
            specification = specification.and(PagamentoSpecifications.hasCondominioId(condominioId));
        }

        return specification;
    }

    private boolean isInadimplente(Cobranca cobranca) {
        return cobranca.getStatus() != StatusCobrancaEnum.PAGA
                && (cobranca.getStatus() == StatusCobrancaEnum.VENCIDA
                || (cobranca.getVencimento() != null && cobranca.getVencimento().isBefore(java.time.LocalDate.now())));
    }

    private BigDecimal somarValores(List<Cobranca> cobrancas) {
        return cobrancas.stream()
                .map(Cobranca::getValor)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }

}
