package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnidadeRepository extends JpaRepository<Unidade, UUID> {
}
