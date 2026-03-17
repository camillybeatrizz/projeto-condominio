package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.CobrancaMapper;
import com.codewithus.kondo.repository.CobrancaRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.service.CobrancaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import com.codewithus.kondo.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CobrancaServiceImpl implements CobrancaService {

    private final CobrancaRepository cobrancaRepository;
    private final UnidadeRepository unidadeRepository;
    private final CobrancaMapper mapper;

    @Override
    public CobrancaResponseDTO salvar(CobrancaRequestDTO dto) {

        validarRegraDeCobranca(dto);

        Unidade unidade = buscarUnidade(dto.unidadeId());
        Cobranca entity = mapper.toEntity(dto, unidade);
        return mapper.toResponseDTO(cobrancaRepository.save(entity));
    }

    @Override
    public CobrancaResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<CobrancaResponseDTO> listar() {
        return cobrancaRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public CobrancaResponseDTO atualizar(UUID id, CobrancaRequestDTO dto) {

        validarRegraDeCobranca(dto);

        Cobranca entity = buscarEntidade(id);
        Unidade unidade = buscarUnidade(dto.unidadeId());
        mapper.updateEntity(entity, dto, unidade);
        return mapper.toResponseDTO(cobrancaRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        cobrancaRepository.delete(buscarEntidade(id));
    }

    private Cobranca buscarEntidade(UUID id) {
        return cobrancaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cobrança não encontrada"));
    }

    private Unidade buscarUnidade(UUID id) {
        return unidadeRepository.findById(id)
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

}
