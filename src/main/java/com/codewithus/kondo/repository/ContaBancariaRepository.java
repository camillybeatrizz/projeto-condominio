package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.ContaBancaria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, UUID>, JpaSpecificationExecutor<ContaBancaria> {

    List<ContaBancaria> findAllByCondominio_IdIn(List<UUID> condominioIds);

    Page<ContaBancaria> findAllByCondominio_Id(UUID condominioId, Pageable pageable);

    Page<ContaBancaria> findAllByCondominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    Optional<ContaBancaria> findByCondominio_IdAndAgenciaAndConta(UUID condominioId, String agencia, String conta);

    boolean existsByCondominio_Id(UUID condominioId);
}
