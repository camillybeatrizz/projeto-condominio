package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CobrancaRepository extends JpaRepository<Cobranca, UUID> {
}
