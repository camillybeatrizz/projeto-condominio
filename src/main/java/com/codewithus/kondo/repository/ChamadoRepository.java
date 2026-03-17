package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, UUID> {
}
