package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Condominio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface CondominioRepository extends JpaRepository<Condominio, UUID> {
    Optional<Condominio> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Condominio> findByCnpj(String cnpj);

    Optional<Condominio> findByCnpjAndDeletedAtIsNull(String cnpj);

    List<Condominio> findAllByIdIn(List<UUID> ids);

    Page<Condominio> findAllByIdIn(List<UUID> ids, Pageable pageable);

    Page<Condominio> findAllByDeletedAtIsNull(Pageable pageable);

    Page<Condominio> findAllByIdInAndDeletedAtIsNull(List<UUID> ids, Pageable pageable);
}
