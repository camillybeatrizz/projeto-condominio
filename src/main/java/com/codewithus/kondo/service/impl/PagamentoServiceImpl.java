package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Pagamento;
import com.codewithus.kondo.dto.pagamento.PagamentoRequestDTO;
import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.PagamentoMapper;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.PagamentoRepository;
import com.codewithus.kondo.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ConflictException;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final CobrancaRepository cobrancaRepository;
    private final PagamentoMapper mapper;

    @Override
    public PagamentoResponseDTO salvar(PagamentoRequestDTO dto) {
        Cobranca cobranca = buscarCobranca(dto.cobrancaId());

        if (cobranca.getStatus() == StatusCobrancaEnum.PAGA){
            throw new BusinessException("Não é permitido registrar pagamento para uma cobrança já paga");
        }

        pagamentoRepository.findByTransactionId(dto.transactionId())
                .ifPresent(existing ->{
                    throw new ConflictException("Já existe pagamento com este transactionId");
                });

        Pagamento entity = mapper.toEntity(dto, cobranca);
        Pagamento saved = pagamentoRepository.save(entity);

        cobranca.setStatus(StatusCobrancaEnum.PAGA);
        cobrancaRepository.save(cobranca);

        return mapper.toResponseDTO(saved);
    }

    @Override
    public PagamentoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<PagamentoResponseDTO> listar() {
        return pagamentoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public PagamentoResponseDTO atualizar(UUID id, PagamentoRequestDTO dto) {
        Pagamento entity = buscarEntidade(id);
        Cobranca cobranca = buscarCobranca(dto.cobrancaId());

        pagamentoRepository.findByTransactionId(dto.transactionId())
                        .ifPresent(existing ->{
                            if (!existing.getId().equals(id)) {
                                throw new ConflictException("Já existe pagamento com este transactionId");
                            }
                        });

        mapper.updateEntity(entity, dto, cobranca);
        return mapper.toResponseDTO(pagamentoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        pagamentoRepository.delete(buscarEntidade(id));
    }

    private Pagamento buscarEntidade(UUID id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado"));
    }

    private Cobranca buscarCobranca(UUID id) {
        return cobrancaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança não encontrada"));
    }
}
