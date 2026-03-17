package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.ContaBancaria;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaRequestDTO;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.ContaBancariaMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.ContaBancariaRepository;
import com.codewithus.kondo.service.ContaBancariaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContaBancariaServiceImpl implements ContaBancariaService {

    private final ContaBancariaRepository contaBancariaRepository;
    private final CondominioRepository condominioRepository;
    private final ContaBancariaMapper mapper;

    @Override
    public ContaBancariaResponseDTO salvar(ContaBancariaRequestDTO dto) {
        Condominio condominio = buscarCondominio(dto.condominioId());
        ContaBancaria entity = mapper.toEntity(dto, condominio);
        return mapper.toResponseDTO(contaBancariaRepository.save(entity));
    }

    @Override
    public ContaBancariaResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<ContaBancariaResponseDTO> listar() {
        return contaBancariaRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ContaBancariaResponseDTO atualizar(UUID id, ContaBancariaRequestDTO dto) {
        ContaBancaria entity = buscarEntidade(id);
        Condominio condominio = buscarCondominio(dto.condominioId());
        mapper.updateEntity(entity, dto, condominio);
        return mapper.toResponseDTO(contaBancariaRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        contaBancariaRepository.delete(buscarEntidade(id));
    }

    private ContaBancaria buscarEntidade(UUID id) {
        return contaBancariaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta bancária não encontrada"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }
}
