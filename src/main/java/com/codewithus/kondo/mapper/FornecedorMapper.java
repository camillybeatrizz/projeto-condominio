package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.dto.fornecedor.FornecedorRequestDTO;
import com.codewithus.kondo.dto.fornecedor.FornecedorResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper {

    public Fornecedor toEntity(FornecedorRequestDTO dto) {
        Fornecedor entity = new Fornecedor();
        entity.setNome(dto.nome());
        entity.setCnpj(dto.cnpj());
        entity.setTelefone(dto.telefone());
        return entity;
    }

    public void updateEntity(Fornecedor entity, FornecedorRequestDTO dto) {
        entity.setNome(dto.nome());
        entity.setCnpj(dto.cnpj());
        entity.setTelefone(dto.telefone());
    }

    public FornecedorResponseDTO toResponseDTO(Fornecedor entity) {
        return new FornecedorResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getTelefone(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
