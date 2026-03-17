package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.ContaBancaria;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaRequestDTO;
import com.codewithus.kondo.dto.contabancaria.ContaBancariaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ContaBancariaMapper {

    public ContaBancaria toEntity(ContaBancariaRequestDTO dto, Condominio condominio) {
        ContaBancaria entity = new ContaBancaria();
        entity.setBanco(dto.banco());
        entity.setAgencia(dto.agencia());
        entity.setConta(dto.conta());
        entity.setTipo(dto.tipo());
        entity.setCondominio(condominio);
        return entity;
    }

    public void updateEntity(ContaBancaria entity, ContaBancariaRequestDTO dto, Condominio condominio) {
        entity.setBanco(dto.banco());
        entity.setAgencia(dto.agencia());
        entity.setConta(dto.conta());
        entity.setTipo(dto.tipo());
        entity.setCondominio(condominio);
    }

    public ContaBancariaResponseDTO toResponseDTO(ContaBancaria entity) {
        return new ContaBancariaResponseDTO(
                entity.getId(),
                entity.getBanco(),
                entity.getAgencia(),
                entity.getConta(),
                entity.getTipo(),
                entity.getCondominio() != null ? entity.getCondominio().getId() : null
        );
    }
}
