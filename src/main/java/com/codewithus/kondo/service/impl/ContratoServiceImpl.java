package com.codewithus.kondo.service.impl;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Contrato;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.dto.contrato.ContratoRequestDTO;
import com.codewithus.kondo.dto.contrato.ContratoResponseDTO;
import com.codewithus.kondo.exception.BusinessException;
import com.codewithus.kondo.exception.ResourceNotFoundException;
import com.codewithus.kondo.mapper.ContratoMapper;
import com.codewithus.kondo.repository.CondominioRepository;
import com.codewithus.kondo.repository.ContratoRepository;
import com.codewithus.kondo.repository.FornecedorRepository;
import com.codewithus.kondo.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CondominioRepository condominioRepository;
    private final ContratoMapper mapper;

    @Override
    public ContratoResponseDTO salvar(ContratoRequestDTO dto) {
        validarDatas(dto);

        Fornecedor fornecedor = buscarFornecedor(dto.fornecedorId());
        Condominio condominio = buscarCondominio(dto.condominioId());
        Contrato entity = mapper.toEntity(dto, fornecedor, condominio);
        return mapper.toResponseDTO(contratoRepository.save(entity));
    }

    @Override
    public ContratoResponseDTO buscarPorId(UUID id) {
        return mapper.toResponseDTO(buscarEntidade(id));
    }

    @Override
    public List<ContratoResponseDTO> listar() {
        return contratoRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    public ContratoResponseDTO atualizar(UUID id, ContratoRequestDTO dto) {
        validarDatas(dto);

        Contrato entity = buscarEntidade(id);
        Fornecedor fornecedor = buscarFornecedor(dto.fornecedorId());
        Condominio condominio = buscarCondominio(dto.condominioId());
        mapper.updateEntity(entity, dto, fornecedor, condominio);
        return mapper.toResponseDTO(contratoRepository.save(entity));
    }

    @Override
    public void deletar(UUID id) {
        contratoRepository.delete(buscarEntidade(id));
    }

    private Contrato buscarEntidade(UUID id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado"));
    }

    private Fornecedor buscarFornecedor(UUID id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor não encontrado"));
    }

    private Condominio buscarCondominio(UUID id) {
        return condominioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Condominio não encontrado"));
    }

    private void validarDatas(ContratoRequestDTO dto){
        if (dto.dataFim() != null && dto.dataFim().isBefore(dto.dataInicio())){
            throw new BusinessException("Data fim não pode ser anterior à data de início");
        }
    }
}
