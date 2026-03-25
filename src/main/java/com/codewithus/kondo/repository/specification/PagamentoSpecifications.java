package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Pagamento;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class PagamentoSpecifications {

    private PagamentoSpecifications() {
    }

    public static Specification<Pagamento> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Pagamento> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cobranca").get("unidade").get("bloco").get("condominio").get("id"), condominioId);
    }

    public static Specification<Pagamento> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("cobranca").get("unidade").get("bloco").get("condominio").get("id").in(condominioIds);
    }

    public static Specification<Pagamento> hasMoradorId(UUID moradorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cobranca").get("unidade").get("morador").get("id"), moradorId);
    }

    public static Specification<Pagamento> dataPagamentoMaiorOuIgual(LocalDate dataInicio) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dataPagamento"), dataInicio);
    }

    public static Specification<Pagamento> dataPagamentoMenorOuIgual(LocalDate dataFim) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("dataPagamento"), dataFim);
    }
}
