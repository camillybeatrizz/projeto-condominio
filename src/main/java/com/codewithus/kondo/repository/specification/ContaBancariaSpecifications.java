package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.ContaBancaria;
import com.codewithus.kondo.domain.enums.TipoContaEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public final class ContaBancariaSpecifications {

    private ContaBancariaSpecifications() {
    }

    public static Specification<ContaBancaria> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("condominio").get("id"), condominioId);
    }

    public static Specification<ContaBancaria> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("condominio").get("id").in(condominioIds);
    }

    public static Specification<ContaBancaria> hasTipo(TipoContaEnum tipo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("tipo"), tipo);
    }

    public static Specification<ContaBancaria> hasBancoContaining(String banco) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("banco")), "%" + banco.toLowerCase() + "%");
    }
}
