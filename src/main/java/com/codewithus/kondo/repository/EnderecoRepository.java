package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {
}
