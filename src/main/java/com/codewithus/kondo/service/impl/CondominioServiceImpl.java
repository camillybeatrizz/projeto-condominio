package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.dto.condominio.CondominioRequestDTO;
import com.codewithus.kondo.dto.condominio.CondominioResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.CondominioMapper;
import com.codewithus.kondo.repository.ContaBancariaRepository;
import com.codewithus.kondo.repository.AreaComumRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.DespesaRepository;
import com.codewithus.kondo.security.AuthenticatedUserFacade;
import com.codewithus.kondo.security.CondominioScopeService;
import com.codewithus.kondo.service.CondominioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.codewithus.kondo.exception.ConflictException;
import com.codewithus.kondo.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CondominioServiceImpl implements CondominioService {

    private final CondominioRepository condominioRepository;
    private final EnderecoRepository enderecoRepository;
    private final BlocoRepository blocoRepository;
    private final AreaComumRepository areaComumRepository;
    private final ContaBancariaRepository contaBancariaRepository;
    private final ContratoRepository contratoRepository;
    private final DespesaRepository despesaRepository;
    private final CondominioMapper mapper;
    private final AuthenticatedUserFacade authenticatedUserFacade;
    private final CondominioScopeService condominioScopeService;

    @Override
    public CondominioResponseDTO salvar(CondominioRequestDTO dto) {
        validarCnpjDuplicado(dto.cnpj(), null);

        Endereco endereco = buscarEndereco(dto.enderecoId());
        Condominio entity = mapper.toEntity(dto, endereco);

        entity = condominioRepository.saveAndFlush(entity);

        return mapper.toResponseDTO(entity);
    }

    @Override
    public CondominioResponseDTO buscarPorId(UUID id) {
        condominioScopeService.assertCanAccessCondominio(id);
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public Page<CondominioResponseDTO> listar(Pageable pageable) {
        Page<Condominio> condominios = authenticatedUserFacade.isAuthenticated() && !authenticatedUserFacade.isAdmin()
                ? condominioRepository.findAllByIdInAndDeletedAtIsNull(condominioScopeService.getCondominioIdsPermitidos(), pageable)
                : condominioRepository.findAllByDeletedAtIsNull(pageable);

        return condominios.map(mapper::toResponseDTO);
    }

    @Override
    public CondominioResponseDTO atualizar(UUID id, CondominioRequestDTO dto) {
        condominioScopeService.assertCanAccessCondominio(id);
        Condominio entity = buscarEntidade(id);

        validarCnpjDuplicado(dto.cnpj(), id);

        Endereco endereco = buscarEndereco(dto.enderecoId());
        mapper.updateEntity(entity, dto, endereco);

        entity = condominioRepository.saveAndFlush(entity);

        return mapper.toResponseDTO(entity);
    }


    @Override
    public void deletar(UUID id) {
        condominioScopeService.assertCanAccessCondominio(id);
        if (blocoRepository.existsByCondominio_IdAndDeletedAtIsNull(id)) {
            throw new BusinessException("Não é permitido excluir condomínio que ainda possui blocos vinculados");
        }
        if (areaComumRepository.existsByCondominio_IdAndDeletedAtIsNull(id)) {
            throw new BusinessException("Não é permitido excluir condomínio que possui áreas comuns vinculadas");
        }
        if (contaBancariaRepository.existsByCondominio_Id(id)) {
            throw new BusinessException("Não é permitido excluir condomínio que possui contas bancárias vinculadas");
        }
        if (contratoRepository.existsByCondominio_Id(id)) {
            throw new BusinessException("Não é permitido excluir condomínio que possui contratos vinculados");
        }
        if (despesaRepository.existsByCondominio_Id(id)) {
            throw new BusinessException("Não é permitido excluir condomínio que possui despesas vinculadas");
        }
        Condominio condominio = buscarEntidade(id);
        condominio.setDeletedAt(java.time.LocalDateTime.now());
        condominio.setDeletedBy(resolveAuditActor());
        condominioRepository.save(condominio);
    }

    private Condominio buscarEntidade(UUID id) {
        return condominioRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private Endereco buscarEndereco(UUID id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado"));
    }

    private void validarCnpjDuplicado(String cnpj, UUID condominioIdAtual) {
        condominioRepository.findByCnpjAndDeletedAtIsNull(cnpj)
                .ifPresent(condominio -> {
                    if (condominioIdAtual == null || !condominio.getId().equals(condominioIdAtual)) {
                        throw new ConflictException("Já existe condomínio com este CNPJ");
                    }
                });
    }

    private String resolveAuditActor() {
        if (!authenticatedUserFacade.isAuthenticated()) {
            return "SYSTEM";
        }

        return authenticatedUserFacade.getRequiredSubject();
    }

}
