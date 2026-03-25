package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.AuditoriaEvento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditoriaEventoRepository extends JpaRepository<AuditoriaEvento, UUID> {

    Page<AuditoriaEvento> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
