package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Pagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID>, JpaSpecificationExecutor<Pagamento> {

    Optional<Pagamento> findByIdAndDeletedAtIsNull(UUID id);

    Optional<Pagamento> findByTransactionId(String transactionId);

    List<Pagamento> findAllByCobranca_Unidade_Morador_Email(String email);

    Page<Pagamento> findAllByCobranca_Unidade_Morador_Email(String email, Pageable pageable);

    Page<Pagamento> findAllByCobranca_Unidade_Morador_EmailAndCobranca_Unidade_Bloco_Condominio_Id(
            String email,
            UUID condominioId,
            Pageable pageable
    );

    List<Pagamento> findAllByCobranca_Unidade_Bloco_Condominio_IdIn(List<UUID> condominioIds);

    Page<Pagamento> findAllByCobranca_Unidade_Bloco_Condominio_Id(UUID condominioId, Pageable pageable);

    Page<Pagamento> findAllByCobranca_Unidade_Bloco_Condominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    boolean existsByIdAndDeletedAtIsNullAndCobranca_Unidade_Morador_Email(UUID id, String email);

    boolean existsByIdAndDeletedAtIsNullAndCobranca_Unidade_Morador_Id(UUID id, UUID moradorId);
}
