package com.codewithus.kondo.repository;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.enums.PerfilEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcessoRepository extends JpaRepository<Acesso, UUID> {
    boolean existsByUsuario_IdAndPerfilAndCondominio_Id(UUID usuarioId, PerfilEnum perfil, UUID condominioId);

    boolean existsByUsuario_IdAndPerfilAndCondominioIsNull(UUID usuarioId, PerfilEnum perfil);

    boolean existsByUnidade_IdAndPerfil(UUID unidadeId, PerfilEnum perfil);

    Optional<Acesso> findByUnidade_IdAndPerfil(UUID unidadeId, PerfilEnum perfil);

    List<Acesso> findAllByUsuario_Email(String email);

    List<Acesso> findAllByUsuario_Id(UUID usuarioId);

    boolean existsByUsuario_Id(UUID usuarioId);

    boolean existsByUsuario_EmailAndCondominio_Id(String email, UUID condominioId);

    boolean existsByUsuario_IdAndCondominio_Id(UUID usuarioId, UUID condominioId);

    @Query("""
            select distinct a.condominio.id
            from Acesso a
            where a.usuario.email = :email
              and a.condominio is not null
            """)
    List<UUID> findDistinctCondominioIdsByUsuario_Email(String email);

    @Query("""
            select distinct a.condominio.id
            from Acesso a
            where a.usuario.id = :usuarioId
              and a.condominio is not null
            """)
    List<UUID> findDistinctCondominioIdsByUsuario_Id(UUID usuarioId);
}
