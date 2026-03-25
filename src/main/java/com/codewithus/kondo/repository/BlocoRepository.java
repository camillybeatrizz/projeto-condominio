package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Bloco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlocoRepository extends JpaRepository<Bloco, UUID> {
    java.util.Optional<Bloco> findByIdAndDeletedAtIsNull(UUID id);

    List<Bloco> findAllByCondominio_IdIn(List<UUID> condominioIds);

    Page<Bloco> findAllByDeletedAtIsNull(Pageable pageable);

    Page<Bloco> findAllByCondominio_Id(UUID condominioId, Pageable pageable);

    Page<Bloco> findAllByCondominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    Page<Bloco> findAllByCondominio_IdAndDeletedAtIsNull(UUID condominioId, Pageable pageable);

    Page<Bloco> findAllByCondominio_IdInAndDeletedAtIsNull(List<UUID> condominioIds, Pageable pageable);

    Optional<Bloco> findByCondominio_IdAndNomeIgnoreCase(UUID condominioId, String nome);

    Optional<Bloco> findByCondominio_IdAndNomeIgnoreCaseAndDeletedAtIsNull(UUID condominioId, String nome);

    boolean existsByCondominio_Id(UUID condominioId);

    boolean existsByCondominio_IdAndDeletedAtIsNull(UUID condominioId);
}
