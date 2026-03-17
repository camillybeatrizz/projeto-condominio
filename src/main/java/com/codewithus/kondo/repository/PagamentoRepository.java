package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

    Optional<Pagamento> findByTransactionId(String transactionId);
}
