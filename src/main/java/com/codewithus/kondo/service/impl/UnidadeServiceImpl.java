package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.UnidadeMapper;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.UnidadeRepository;
import com.codewithus.kondo.service.UnidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnidadeServiceImpl implements UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final BlocoRepository blocoRepository;
    private final UnidadeMapper mapper;

    @Override
    public UnidadeResponseDTO salvar(UnidadeRequestDTO dto) {
        Bloco bloco = buscarBloco(dto.blocoId());
        Unidade entity = mapper.toEntity(dto, bloco);
        return mapper.toResponseDTO(unidadeRepository.save(entity));
    }

    @Override
    public UnidadeResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<UnidadeResponseDTO> listar() {
        return unidadeRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public UnidadeResponseDTO atualizar(UUID id, UnidadeRequestDTO dto) {
        Unidade entity = buscarEntidade(id);
        Bloco bloco = buscarBloco(dto.blocoId());
        mapper.updateEntity(entity, dto, bloco);
        return mapper.toResponseDTO(unidadeRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        unidadeRepository.delete(buscarEntidade(id));
    }

    private Unidade buscarEntidade(UUID id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada"));
    }

    private Bloco buscarBloco(UUID id) {
        return blocoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloco não encontrado"));
    }
}
