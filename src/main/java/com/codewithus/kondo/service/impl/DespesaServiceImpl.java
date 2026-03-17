package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Despesa;
import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.DespesaMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.DespesaRepository;
import com.codewithus.kondo.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DespesaServiceImpl implements DespesaService {

    private final DespesaRepository despesaRepository;
    private final CondominioRepository condominioRepository;
    private final DespesaMapper mapper;

    @Override
    public DespesaResponseDTO salvar(DespesaRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        Despesa entity = mapper.toEntity(dto, condominio);
        return mapper.toResponseDTO(despesaRepository.save(entity));
    }

    @Override
    public DespesaResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<DespesaResponseDTO> listar() {
        return despesaRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public DespesaResponseDTO atualizar(UUID id, DespesaRequestDTO dto) {
        Despesa entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());
        mapper.updateEntity(entity, dto, condominio);
        return mapper.toResponseDTO(despesaRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        despesaRepository.delete(buscarEntidade(id));
    }

    private Despesa buscarEntidade(UUID id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }
}
