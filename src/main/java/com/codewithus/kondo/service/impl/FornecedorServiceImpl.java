package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.FornecedorMapper;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper mapper;

    @Override
    public FornecedorResponseDTO salvar(FornecedorRequestDTO dto) {
        Fornecedor entity = mapper.toEntity(dto);
        return mapper.toResponseDTO(fornecedorRepository.save(entity));
    }

    @Override
    public FornecedorResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<FornecedorResponseDTO> listar() {
        return fornecedorRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public FornecedorResponseDTO atualizar(UUID id, FornecedorRequestDTO dto) {
        Fornecedor entity = buscarEntidade(id);
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(fornecedorRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        fornecedorRepository.delete(buscarEntidade(id));
    }

    private Fornecedor buscarEntidade(UUID id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
    }
}
