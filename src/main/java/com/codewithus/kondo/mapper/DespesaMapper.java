package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Despesa;
import com.codewithus.kondo.dto.despesa.DespesaRequestDTO;
import com.codewithus.kondo.dto.despesa.DespesaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class DespesaMapper {

    public Despesa toEntity(DespesaRequestDTO dto, Condominio condominio) {
        Despesa entity = new Despesa();
        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
        entity.setData(dto.data());
        entity.setCategoria(dto.categoria());
        entity.setCondominio(condominio);
        return entity;
    }

    public void updateEntity(Despesa entity, DespesaRequestDTO dto, Condominio condominio) {
        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
        entity.setData(dto.data());
        entity.setCategoria(dto.categoria());
        entity.setCondominio(condominio);
    }

    public DespesaResponseDTO toResponseDTO(Despesa entity) {
        return new DespesaResponseDTO(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getData(),
                entity.getCategoria(),
                entity.getCondominio() != null ? entity.getCondominio().getId() : null
        );
    }
}
