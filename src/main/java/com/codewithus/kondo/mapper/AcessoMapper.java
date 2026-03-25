package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Acesso;
import com.codewithus.kondo.domain.entity.Condominio;
import com.codewithus.kondo.domain.entity.Unidade;
import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.acesso.AcessoRequestDTO;
import com.codewithus.kondo.dto.acesso.AcessoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AcessoMapper {

    public Acesso toEntity(AcessoRequestDTO dto, Usuario usuario, Condominio condominio, Unidade unidade) {
        Acesso entity = new Acesso();
        entity.setUsuario(usuario);
        entity.setCondominio(condominio);
        entity.setUnidade(unidade);
        entity.setPerfil(dto.perfil());
        return entity;
    }

    public void updateEntity(Acesso entity, AcessoRequestDTO dto, Usuario usuario, Condominio condominio, Unidade unidade) {
        entity.setUsuario(usuario);
        entity.setCondominio(condominio);
        entity.setUnidade(unidade);
        entity.setPerfil(dto.perfil());
    }

    public AcessoResponseDTO toResponseDTO(Acesso entity) {
        return new AcessoResponseDTO(
                entity.getId(),
                entity.getUsuario() != null ? entity.getUsuario().getId() : null,
                entity.getCondominio() != null ? entity.getCondominio().getId() : null,
                entity.getUnidade() != null ? entity.getUnidade().getId() : null,
                entity.getPerfil()
        );
    }
}
