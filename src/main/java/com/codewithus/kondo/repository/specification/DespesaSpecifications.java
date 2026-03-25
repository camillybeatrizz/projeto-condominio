package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Despesa;
import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class DespesaSpecifications {

    private DespesaSpecifications() {
    }

    public static Specification<Despesa> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Despesa> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("condominio").get("id"), condominioId);
    }

    public static Specification<Despesa> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("condominio").get("id").in(condominioIds);
    }

    public static Specification<Despesa> hasCategoria(CategoriaDespesaEnum categoria) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categoria"), categoria);
    }

    public static Specification<Despesa> hasDataMaiorOuIgual(LocalDate dataInicio) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("data"), dataInicio);
    }

    public static Specification<Despesa> hasDataMenorOuIgual(LocalDate dataFim) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("data"), dataFim);
    }
}
