package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Chamado;
import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

public final class ChamadoSpecifications {

    private ChamadoSpecifications() {
    }

    public static Specification<Chamado> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Chamado> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("unidade").get("bloco").get("condominio").get("id"), condominioId);
    }

    public static Specification<Chamado> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("unidade").get("bloco").get("condominio").get("id").in(condominioIds);
    }

    public static Specification<Chamado> hasMoradorId(UUID moradorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("unidade").get("morador").get("id"), moradorId);
    }

    public static Specification<Chamado> hasStatus(StatusChamadoEnum status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }
}
