package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Chamado;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.dto.chamado.ChamadoRequestDTO;
import com.codewithus.kondo.dto.chamado.ChamadoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ChamadoMapper {

    public Chamado toEntity(ChamadoRequestDTO dto, Unidade unidade) {
        Chamado entity = new Chamado();
        entity.setDescricao(dto.descricao());
        entity.setStatus(dto.status());
        entity.setDataAbertura(dto.dataAbertura());
        entity.setUnidade(unidade);
        return entity;
    }

    public void updateEntity(Chamado entity, ChamadoRequestDTO dto, Unidade unidade) {
        entity.setDescricao(dto.descricao());
        entity.setStatus(dto.status());
        entity.setDataAbertura(dto.dataAbertura());
        entity.setUnidade(unidade);
    }

    public ChamadoResponseDTO toResponseDTO(Chamado entity) {
        return new ChamadoResponseDTO(
                entity.getId(),
                entity.getDescricao(),
                entity.getStatus(),
                entity.getDataAbertura(),
                entity.getUnidade() != null ? entity.getUnidade().getId() : null
        );
    }
}
