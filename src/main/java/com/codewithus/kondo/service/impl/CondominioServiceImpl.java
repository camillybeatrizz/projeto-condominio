package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.dto.condominio.CondominioRequestDTO;
import com.codewithus.kondo.dto.condominio.CondominioResponseDTO;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.CondominioMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.EnderecoRepository;
import com.codewithus.kondo.service.CondominioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.codewithus.kondo.exception.ConflictException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CondominioServiceImpl implements CondominioService {

    private final CondominioRepository condominioRepository;
    private final EnderecoRepository enderecoRepository;
    private final CondominioMapper mapper;

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
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<CondominioResponseDTO> listar() {
        return condominioRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public CondominioResponseDTO atualizar(UUID id, CondominioRequestDTO dto) {
        Condominio entity = buscarEntidade(id);

        validarCnpjDuplicado(dto.cnpj(), id);

        Endereco endereco = buscarEndereco(dto.enderecoId());
        mapper.updateEntity(entity, dto, endereco);

        entity = condominioRepository.saveAndFlush(entity);

        return mapper.toResponseDTO(entity);
    }


    @Override
    public void deletar(UUID id) {
        condominioRepository.delete(buscarEntidade(id));
    }

    private Condominio buscarEntidade(UUID id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private Endereco buscarEndereco(UUID id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado"));
    }

    private void validarCnpjDuplicado(String cnpj, UUID condominioIdAtual) {
        condominioRepository.findByCnpj(cnpj)
                .ifPresent(condominio -> {
                    if (condominioIdAtual == null || !condominio.getId().equals(condominioIdAtual)) {
                        throw new ConflictException("Já existe condomínio com este CNPJ");
                    }
                });
    }

}
