package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.dto.bloco.BlocoRequestDTO;
import com.codewithus.kondo.dto.bloco.BlocoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class BlocoMapper {

    public Bloco toEntity(BlocoRequestDTO dto, Condominio condominio) {
        Bloco entity = new Bloco();
        entity.setNome(dto.nome());
        entity.setCondominio(condominio);
        return entity;
    }

    public void updateEntity(Bloco entity, BlocoRequestDTO dto, Condominio condominio) {
        entity.setNome(dto.nome());
        entity.setCondominio(condominio);
    }

    public BlocoResponseDTO toResponseDTO(Bloco entity) {
        return new BlocoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCondominio() != null ? entity.getCondominio().getId() : null
        );
    }
}
