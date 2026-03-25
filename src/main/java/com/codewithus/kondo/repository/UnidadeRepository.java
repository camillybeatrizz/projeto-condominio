package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Unidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnidadeRepository extends JpaRepository<Unidade, UUID>, JpaSpecificationExecutor<Unidade> {
    Optional<Unidade> findByIdAndDeletedAtIsNull(UUID id);

    List<Unidade> findAllByMorador_Email(String email);

    Page<Unidade> findAllByMorador_Email(String email, Pageable pageable);

    Page<Unidade> findAllByMorador_EmailAndBloco_Condominio_Id(String email, UUID condominioId, Pageable pageable);

    List<Unidade> findAllByBloco_Condominio_IdIn(List<UUID> condominioIds);

    List<Unidade> findAllByBloco_Condominio_Id(UUID condominioId);

    Page<Unidade> findAllByBloco_Condominio_Id(UUID condominioId, Pageable pageable);

    Page<Unidade> findAllByBloco_Condominio_IdIn(List<UUID> condominioIds, Pageable pageable);

    Optional<Unidade> findByBloco_IdAndNumeroIgnoreCase(UUID blocoId, String numero);

    Optional<Unidade> findByBloco_IdAndNumeroIgnoreCaseAndDeletedAtIsNull(UUID blocoId, String numero);

    boolean existsByIdAndMorador_Email(UUID id, String email);

    boolean existsByIdAndMorador_Id(UUID id, UUID moradorId);

    boolean existsByMorador_IdAndBloco_Condominio_Id(UUID moradorId, UUID condominioId);

    @org.springframework.data.jpa.repository.Query("""
            select distinct u.bloco.condominio.id
            from Unidade u
            where u.morador.id = :moradorId
            """)
    List<UUID> findDistinctCondominioIdsByMorador_Id(UUID moradorId);

    boolean existsByBloco_Id(UUID blocoId);

    boolean existsByBloco_IdAndDeletedAtIsNull(UUID blocoId);
}
