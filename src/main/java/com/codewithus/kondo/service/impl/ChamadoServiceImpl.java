package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Chamado;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.ChamadoMapper;
import com.codewithus.kondo.repository.ChamadoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.service.ChamadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChamadoServiceImpl implements ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UnidadeRepository unidadeRepository;
    private final ChamadoMapper mapper;

    @Override
    public ChamadoResponseDTO salvar(ChamadoRequestDTO dto) {
        Unidade unidade = buscarUnidade(dto.unidadeId());
        Chamado entity = mapper.toEntity(dto, unidade);
        return mapper.toResponseDTO(chamadoRepository.save(entity));
    }

    @Override
    public ChamadoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<ChamadoResponseDTO> listar() {
        return chamadoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ChamadoResponseDTO atualizar(UUID id, ChamadoRequestDTO dto) {
        Chamado entity = buscarEntidade(id);
        Unidade unidade = buscarUnidade(dto.unidadeId());
        mapper.updateEntity(entity, dto, unidade);
        return mapper.toResponseDTO(chamadoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        chamadoRepository.delete(buscarEntidade(id));
    }

    private Chamado buscarEntidade(UUID id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado"));
    }

    private Unidade buscarUnidade(UUID id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }
}
