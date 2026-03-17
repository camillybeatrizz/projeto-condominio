package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {
}
