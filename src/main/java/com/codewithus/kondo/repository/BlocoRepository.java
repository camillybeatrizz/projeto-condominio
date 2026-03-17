package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlocoRepository extends JpaRepository<Bloco, UUID> {
}
