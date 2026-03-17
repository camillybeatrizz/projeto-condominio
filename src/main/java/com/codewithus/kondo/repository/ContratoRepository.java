package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContratoRepository extends JpaRepository<Contrato, UUID> {
}
