package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.AreaComum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AreaComumRepository extends JpaRepository<AreaComum, UUID> {

    Optional<AreaComum> findByIdAndDeletedAtIsNull(UUID id);

    Optional<AreaComum> findByCondominio_IdAndNomeIgnoreCaseAndDeletedAtIsNull(UUID condominioId, String nome);

    Page<AreaComum> findAllByCondominio_IdAndDeletedAtIsNull(UUID condominioId, Pageable pageable);

    Page<AreaComum> findAllByCondominio_IdInAndDeletedAtIsNull(List<UUID> condominioIds, Pageable pageable);

    Page<AreaComum> findAllByDeletedAtIsNull(Pageable pageable);

    boolean existsByCondominio_IdAndDeletedAtIsNull(UUID condominioId);
}
