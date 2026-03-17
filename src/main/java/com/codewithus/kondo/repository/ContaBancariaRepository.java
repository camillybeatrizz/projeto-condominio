package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, UUID> {
}
