package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Contrato;
import com.codewithus.kondo.domain.entity.Fornecedor;
import com.codewithus.kondo.dto.contrato.ContratoRequestDTO;
import com.codewithus.kondo.dto.contrato.ContratoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ContratoMapper {

    public Contrato toEntity(ContratoRequestDTO dto, Fornecedor fornecedor, Condominio condominio) {
        Contrato entity = new Contrato();
        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
        entity.setDataInicio(dto.dataInicio());
        entity.setDataFim(dto.dataFim());
        entity.setFornecedor(fornecedor);
        entity.setCondominio(condominio);
        return entity;
    }

    public void updateEntity(Contrato entity, ContratoRequestDTO dto, Fornecedor fornecedor, Condominio condominio) {
        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
        entity.setDataInicio(dto.dataInicio());
        entity.setDataFim(dto.dataFim());
        entity.setFornecedor(fornecedor);
        entity.setCondominio(condominio);
    }

    public ContratoResponseDTO toResponseDTO(Contrato entity) {
        return new ContratoResponseDTO(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getFornecedor() != null ? entity.getFornecedor().getId() : null,
                entity.getCondominio() != null ? entity.getCondominio().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
