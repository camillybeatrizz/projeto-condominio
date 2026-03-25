package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Cobranca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CobrancaRepository extends JpaRepository<Cobranca, UUID>, JpaSpecificationExecutor<Cobranca> {

    List<Cobranca> findAllByUnidade_Morador_Email(String email);

    Page<Cobranca> findAllByUnidade_Morador_Email(String email, Pageable pageable);

    Page<Cobranca> findAllByUnidade_Morador_EmailAndUnidade_Bloco_Condominio_Id(
            String email,
            UUID condominioId,
            Pageable pageable
    );

    List<Cobranca> findAllByUnidade_Bloco_Condominio_IdIn(List<UUID> condominioIds);

    Page<Cobranca> findAllByUnidade_Bloco_Condominio_Id(UUID condominioId, Pageable pageable);

    Page<Cobranca> findAllByUnidade_Bloco_Condominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    Optional<Cobranca> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByUnidade_IdAndDeletedAtIsNull(UUID unidadeId);

    boolean existsByUnidade_IdAndCompetenciaIgnoreCaseAndDeletedAtIsNull(UUID unidadeId, String competencia);

    boolean existsByIdAndDeletedAtIsNullAndUnidade_Morador_Email(UUID id, String email);

    boolean existsByIdAndDeletedAtIsNullAndUnidade_Morador_Id(UUID id, UUID moradorId);

    Optional<Cobranca> findByReferenciaExterna(String referenciaExterna);

    Optional<Cobranca> findByReferenciaExternaAndDeletedAtIsNull(String referenciaExterna);

    boolean existsByUnidade_IdAndCompetenciaIgnoreCase(UUID unidadeId, String competencia);
}
