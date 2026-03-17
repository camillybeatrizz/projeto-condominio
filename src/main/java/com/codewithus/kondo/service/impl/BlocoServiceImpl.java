package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.BlocoMapper;
import com.codewithus.kondo.repository.BlocoRepository;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.service.BlocoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlocoServiceImpl implements BlocoService {

    private final BlocoRepository blocoRepository;
    private final CondominioRepository condominioRepository;
    private final BlocoMapper mapper;

    @Override
    public BlocoResponseDTO salvar(BlocoRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        Bloco entity = mapper.toEntity(dto, condominio);
        return mapper.toResponseDTO(blocoRepository.save(entity));
    }

    @Override
    public BlocoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<BlocoResponseDTO> listar() {
        return blocoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public BlocoResponseDTO atualizar(UUID id, BlocoRequestDTO dto) {
        Bloco entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());
        mapper.updateEntity(entity, dto, condominio);
        return mapper.toResponseDTO(blocoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        blocoRepository.delete(buscarEntidade(id));
    }

    private Bloco buscarEntidade(UUID id) {
        return blocoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bloco não encontrado"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }
}
