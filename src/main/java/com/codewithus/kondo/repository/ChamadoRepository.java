package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Chamado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, UUID>, JpaSpecificationExecutor<Chamado> {

    List<Chamado> findAllByUnidade_Morador_Email(String email);

    Page<Chamado> findAllByUnidade_Morador_Email(String email, Pageable pageable);

    Page<Chamado> findAllByUnidade_Morador_EmailAndUnidade_Bloco_Condominio_Id(
            String email,
            UUID condominioId,
            Pageable pageable
    );

    List<Chamado> findAllByUnidade_Bloco_Condominio_IdIn(List<UUID> condominioIds);

    Page<Chamado> findAllByUnidade_Bloco_Condominio_Id(UUID condominioId, Pageable pageable);

    Page<Chamado> findAllByUnidade_Bloco_Condominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    java.util.Optional<Chamado> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByUnidade_IdAndDeletedAtIsNull(UUID unidadeId);

    boolean existsByIdAndDeletedAtIsNullAndUnidade_Morador_Email(UUID id, String email);

    boolean existsByIdAndDeletedAtIsNullAndUnidade_Morador_Id(UUID id, UUID moradorId);
}
