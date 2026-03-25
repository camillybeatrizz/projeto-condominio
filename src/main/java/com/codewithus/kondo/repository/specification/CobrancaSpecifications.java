package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Cobranca;
import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class CobrancaSpecifications {

    private CobrancaSpecifications() {
    }

    public static Specification<Cobranca> isNotDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

    public static Specification<Cobranca> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("unidade").get("bloco").get("condominio").get("id"), condominioId);
    }

    public static Specification<Cobranca> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("unidade").get("bloco").get("condominio").get("id").in(condominioIds);
    }

    public static Specification<Cobranca> hasMoradorId(UUID moradorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("unidade").get("morador").get("id"), moradorId);
    }

    public static Specification<Cobranca> hasStatus(StatusCobrancaEnum status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Cobranca> hasCompetencia(String competencia) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("competencia")), competencia.toLowerCase());
    }

    public static Specification<Cobranca> isInadimplente(LocalDate hoje) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.notEqual(root.get("status"), StatusCobrancaEnum.PAGA),
                        criteriaBuilder.or(
                                criteriaBuilder.lessThan(root.get("vencimento"), hoje),
                                criteriaBuilder.equal(root.get("status"), StatusCobrancaEnum.VENCIDA)
                        )
                );
    }
}
