package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface CondominioRepository extends JpaRepository<Condominio, UUID> {
    Optional<Condominio> findByCnpj(String cnpj);
}
