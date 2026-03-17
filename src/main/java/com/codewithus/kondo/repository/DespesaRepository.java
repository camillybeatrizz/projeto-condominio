package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DespesaRepository extends JpaRepository<Despesa, UUID> {
}
