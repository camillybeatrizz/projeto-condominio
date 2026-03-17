package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.entity.Pagamento;
import com.codewithus.kondo.dto.pagamento.PagamentoRequestDTO;
import com.codewithus.kondo.dto.pagamento.PagamentoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public Pagamento toEntity(PagamentoRequestDTO dto, Cobranca cobranca) {
        Pagamento entity = new Pagamento();
        entity.setValor(dto.valor());
        entity.setDataPagamento(dto.dataPagamento());
        entity.setForma(dto.forma());
        entity.setTransactionId(dto.transactionId());
        entity.setCobranca(cobranca);
        return entity;
    }

    public void updateEntity(Pagamento entity, PagamentoRequestDTO dto, Cobranca cobranca) {
        entity.setValor(dto.valor());
        entity.setDataPagamento(dto.dataPagamento());
        entity.setForma(dto.forma());
        entity.setTransactionId(dto.transactionId());
        entity.setCobranca(cobranca);
    }

    public PagamentoResponseDTO toResponseDTO(Pagamento entity) {
        return new PagamentoResponseDTO(
                entity.getId(),
                entity.getValor(),
                entity.getDataPagamento(),
                entity.getForma(),
                entity.getTransactionId(),
                entity.getCobranca() != null ? entity.getCobranca().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
