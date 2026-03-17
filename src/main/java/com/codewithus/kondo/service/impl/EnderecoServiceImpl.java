package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.dto.endereco.EnderecoRequestDTO;
import com.codewithus.kondo.dto.endereco.EnderecoResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.EnderecoMapper;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.service.EnderecoService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EnderecoServiceImpl implements EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final EnderecoMapper mapper;

    @Override
    public EnderecoResponseDTO salvar(EnderecoRequestDTO dto) {
        Endereco entity = mapper.toEntity(dto);
        return mapper.toResponseDTO(enderecoRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @Override
    public EnderecoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnderecoResponseDTO> listar() {
        return enderecoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public EnderecoResponseDTO atualizar(UUID id, EnderecoRequestDTO dto) {
        Endereco entity = buscarEntidade(id);
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(enderecoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        enderecoRepository.delete(buscarEntidade(id));
    }

    private Endereco buscarEntidade(UUID id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
    }
}
