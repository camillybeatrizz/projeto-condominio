package com.codewithus.kondo.repository.specification;

import com.codewithus.kondo.domain.entity.Contrato;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class ContratoSpecifications {

    private ContratoSpecifications() {
    }

    public static Specification<Contrato> hasCondominioId(UUID condominioId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("condominio").get("id"), condominioId);
    }

    public static Specification<Contrato> hasCondominioIdIn(List<UUID> condominioIds) {
        return (root, query, criteriaBuilder) ->
                root.get("condominio").get("id").in(condominioIds);
    }

    public static Specification<Contrato> hasFornecedorId(UUID fornecedorId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fornecedor").get("id"), fornecedorId);
    }

    public static Specification<Contrato> vigenciaTerminaAposOuSemFim(LocalDate dataInicio) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.isNull(root.get("dataFim")),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dataFim"), dataInicio)
                );
    }

    public static Specification<Contrato> vigenciaComecaAntesOuNoFim(LocalDate dataFim) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("dataInicio"), dataFim);
    }
}
