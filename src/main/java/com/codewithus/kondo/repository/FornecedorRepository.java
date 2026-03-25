package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID>, JpaSpecificationExecutor<Fornecedor> {

    Optional<Fornecedor> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Fornecedor> findByCnpj(String cnpj);

    Optional<Fornecedor> findByCnpjAndDeletedAtIsNull(String cnpj);

    @Query("""
            select distinct f
            from Fornecedor f
            join Contrato c on c.fornecedor = f
            where c.condominio.id in :condominioIds
            """)
    List<Fornecedor> findDistinctByContratosCondominioIds(List<UUID> condominioIds);

    @Query("""
            select count(f) > 0
            from Fornecedor f
            join Contrato c on c.fornecedor = f
            where f.id = :fornecedorId
              and c.condominio.id in :condominioIds
            """)
    boolean existsByIdAndContratoCondominioIdIn(UUID fornecedorId, List<UUID> condominioIds);

    @Query("""
            select count(f) > 0
            from Fornecedor f
            join Contrato c on c.fornecedor = f
            where f.id = :fornecedorId
              and f.deletedAt is null
              and c.condominio.id in :condominioIds
            """)
    boolean existsByIdAndDeletedAtIsNullAndContratoCondominioIdIn(UUID fornecedorId, List<UUID> condominioIds);

    @Query(
            value = """
                    select distinct f
                    from Fornecedor f
                    join Contrato c on c.fornecedor = f
                    where c.condominio.id in :condominioIds
                      and (:condominioId is null or c.condominio.id = :condominioId)
                    """,
            countQuery = """
                    select count(distinct f.id)
                    from Fornecedor f
                    join Contrato c on c.fornecedor = f
                    where c.condominio.id in :condominioIds
                      and (:condominioId is null or c.condominio.id = :condominioId)
                    """
    )
    Page<Fornecedor> findDistinctByContratosCondominioIds(List<UUID> condominioIds, UUID condominioId, Pageable pageable);

    @Query(
            value = """
                    select distinct f
                    from Fornecedor f
                    join Contrato c on c.fornecedor = f
                    where (:condominioId is null or c.condominio.id = :condominioId)
                    """,
            countQuery = """
                    select count(distinct f.id)
                    from Fornecedor f
                    join Contrato c on c.fornecedor = f
                    where (:condominioId is null or c.condominio.id = :condominioId)
                    """
    )
    Page<Fornecedor> findDistinctByContratos(UUID condominioId, Pageable pageable);
}
