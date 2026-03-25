package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.AreaComum;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.dto.areacomum.AreaComumRequestDTO;
import com.codewithus.kondo.dto.areacomum.AreaComumResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AreaComumMapper {

    public AreaComum toEntity(AreaComumRequestDTO dto, Condominio condominio) {
        AreaComum entity = new AreaComum();
        entity.setNome(dto.nome());
        entity.setDescricao(dto.descricao());
        entity.setCapacidade(dto.capacidade());
        entity.setCondominio(condominio);
        return entity;
    }

    public void updateEntity(AreaComum entity, AreaComumRequestDTO dto, Condominio condominio) {
        entity.setNome(dto.nome());
        entity.setDescricao(dto.descricao());
        entity.setCapacidade(dto.capacidade());
        entity.setCondominio(condominio);
    }

    public AreaComumResponseDTO toResponseDTO(AreaComum entity) {
        return new AreaComumResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getCapacidade(),
                entity.getCondominio() != null ? entity.getCondominio().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
