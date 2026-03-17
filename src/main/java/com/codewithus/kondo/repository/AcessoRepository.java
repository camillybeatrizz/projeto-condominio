package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AcessoRepository extends JpaRepository<Acesso, UUID> {
}
