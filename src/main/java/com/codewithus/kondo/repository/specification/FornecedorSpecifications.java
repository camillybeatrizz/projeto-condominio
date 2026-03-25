package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Contrato;
import com.codewithus.kondo.domain.entity.Fornecedor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public final class FornecedorSpecifications {

    private FornecedorSpecifications() {
    }

    public static Specification<Fornecedor> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Fornecedor> hasContrato() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            var subquery = query.subquery(UUID.class);
            var contrato = subquery.from(Contrato.class);
            subquery.select(contrato.get("id"))
                    .where(criteriaBuilder.equal(contrato.get("fornecedor").get("id"), root.get("id")));
            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<Fornecedor> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            var subquery = query.subquery(UUID.class);
            var contrato = subquery.from(Contrato.class);
            subquery.select(contrato.get("id"))
                    .where(
                            criteriaBuilder.equal(contrato.get("fornecedor").get("id"), root.get("id")),
                            criteriaBuilder.equal(contrato.get("condominio").get("id"), condominioId)
                    );
            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<Fornecedor> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            var subquery = query.subquery(UUID.class);
            var contrato = subquery.from(Contrato.class);
            subquery.select(contrato.get("id"))
                    .where(
                            criteriaBuilder.equal(contrato.get("fornecedor").get("id"), root.get("id")),
                            contrato.get("condominio").get("id").in(condominioIds)
                    );
            return criteriaBuilder.exists(subquery);
        };
    }

    public static Specification<Fornecedor> hasNomeContaining(String nome) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Fornecedor> hasCnpjContaining(String cnpj) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("cnpj")), "%" + cnpj.toLowerCase() + "%");
    }
}
