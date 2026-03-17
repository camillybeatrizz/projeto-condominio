package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.dto.cobranca.CobrancaRequestDTO;
import com.codewithus.kondo.dto.cobranca.CobrancaResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CobrancaMapper {

    public Cobranca toEntity(CobrancaRequestDTO dto, Unidade unidade) {
        Cobranca entity = new Cobranca();
        entity.setValor(dto.valor());
        entity.setVencimento(dto.vencimento());
        entity.setStatus(dto.status());
        entity.setCompetencia(dto.competencia());
        entity.setUnidade(unidade);
        return entity;
    }

    public void updateEntity(Cobranca entity, CobrancaRequestDTO dto, Unidade unidade) {
        entity.setValor(dto.valor());
        entity.setVencimento(dto.vencimento());
        entity.setStatus(dto.status());
        entity.setCompetencia(dto.competencia());
        entity.setUnidade(unidade);
    }

    public CobrancaResponseDTO toResponseDTO(Cobranca entity) {
        return new CobrancaResponseDTO(
                entity.getId(),
                entity.getValor(),
                entity.getVencimento(),
                entity.getStatus(),
                entity.getCompetencia(),
                entity.getUnidade() != null ? entity.getUnidade().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
