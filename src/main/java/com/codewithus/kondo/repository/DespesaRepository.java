package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface DespesaRepository extends JpaRepository<Despesa, UUID>, JpaSpecificationExecutor<Despesa> {

    java.util.Optional<Despesa> findByIdAndDeletedAtIsNull(UUID id);

    List<Despesa> findAllByCondominio_IdIn(List<UUID> condominioIds);

    Page<Despesa> findAllByCondominio_Id(UUID condominioId, Pageable pageable);

    Page<Despesa> findAllByCondominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    boolean existsByCondominio_Id(UUID condominioId);
}
