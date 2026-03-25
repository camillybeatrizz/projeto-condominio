package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Contrato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ContratoRepository extends JpaRepository<Contrato, UUID>, JpaSpecificationExecutor<Contrato> {

    List<Contrato> findAllByCondominio_IdIn(List<UUID> condominioIds);

    Page<Contrato> findAllByCondominio_Id(UUID condominioId, Pageable pageable);

    Page<Contrato> findAllByCondominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    boolean existsByFornecedor_Id(UUID fornecedorId);

    boolean existsByCondominio_Id(UUID condominioId);

    @Query("""
            select count(c) > 0
            from Contrato c
            where c.fornecedor.id = :fornecedorId
              and c.condominio.id = :condominioId
              and (:contratoAtualId is null or c.id <> :contratoAtualId)
              and (c.dataFim is null or c.dataFim >= :dataInicio)
              and (:dataFim is null or c.dataInicio <= :dataFim)
            """)
    boolean existsContratoSobreposto(
            @Param("fornecedorId") UUID fornecedorId,
            @Param("condominioId") UUID condominioId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("contratoAtualId") UUID contratoAtualId
    );
}
