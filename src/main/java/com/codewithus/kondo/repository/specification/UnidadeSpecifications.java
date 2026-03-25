package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Unidade;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public final class UnidadeSpecifications {

    private UnidadeSpecifications() {
    }

    public static Specification<Unidade> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Unidade> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("bloco").get("condominio").get("id"), condominioId);
    }

    public static Specification<Unidade> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("bloco").get("condominio").get("id").in(condominioIds);
    }

    public static Specification<Unidade> hasMoradorId(UUID moradorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("morador").get("id"), moradorId);
    }

    public static Specification<Unidade> hasBlocoId(UUID blocoId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("bloco").get("id"), blocoId);
    }

    public static Specification<Unidade> hasTipo(String tipo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("tipo")), tipo.toLowerCase());
    }

    public static Specification<Unidade> hasNumeroContaining(String numero) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("numero")), "%" + numero.toLowerCase() + "%");
    }
}
