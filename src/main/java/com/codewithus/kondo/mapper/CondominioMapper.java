package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.dto.condominio.CondominioRequestDTO;
import com.codewithus.kondo.dto.condominio.CondominioResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CondominioMapper {

    public Condominio toEntity(CondominioRequestDTO dto, Endereco endereco) {
        Condominio entity = new Condominio();
        entity.setNome(dto.nome());
        entity.setCnpj(dto.cnpj());
        entity.setTelefone(dto.telefone());
        entity.setEndereco(endereco);
        return entity;
    }

    public void updateEntity(Condominio entity, CondominioRequestDTO dto, Endereco endereco) {
        entity.setNome(dto.nome());
        entity.setCnpj(dto.cnpj());
        entity.setTelefone(dto.telefone());
        entity.setEndereco(endereco);
    }

    public CondominioResponseDTO toResponseDTO(Condominio entity) {
        return new CondominioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getTelefone(),
                entity.getEndereco() != null ? entity.getEndereco().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
