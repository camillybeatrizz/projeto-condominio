package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Bloco;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.dto.unidade.UnidadeRequestDTO;
import com.codewithus.kondo.dto.unidade.UnidadeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UnidadeMapper {

    public Unidade toEntity(UnidadeRequestDTO dto, Bloco bloco) {
        Unidade entity = new Unidade();
        entity.setNumero(dto.numero());
        entity.setAndar(dto.andar());
        entity.setTipo(dto.tipo());
        entity.setBloco(bloco);
        return entity;
    }

    public void updateEntity(Unidade entity, UnidadeRequestDTO dto, Bloco bloco) {
        entity.setNumero(dto.numero());
        entity.setAndar(dto.andar());
        entity.setTipo(dto.tipo());
        entity.setBloco(bloco);
    }

    public UnidadeResponseDTO toResponseDTO(Unidade entity) {
        return new UnidadeResponseDTO(
                entity.getId(),
                entity.getNumero(),
                entity.getAndar(),
                entity.getTipo(),
                entity.getBloco() != null ? entity.getBloco().getId() : null
        );
    }
}
