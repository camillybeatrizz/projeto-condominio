package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Usuario;
import com.codewithus.kondo.dto.usuario.UsuarioRequestDTO;
import com.codewithus.kondo.dto.usuario.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario entity = new Usuario();
        entity.setNome(dto.nome());
        entity.setEmail(dto.email());
        entity.setSenha(dto.senha());
        entity.setTelefone(dto.telefone());
        entity.setAtivo(dto.ativo());
        return entity;
    }

    public void updateEntity(Usuario entity, UsuarioRequestDTO dto) {
        entity.setNome(dto.nome());
        entity.setEmail(dto.email());
        entity.setSenha(dto.senha());
        entity.setTelefone(dto.telefone());
        entity.setAtivo(dto.ativo());
    }

    public UsuarioResponseDTO toResponseDTO(Usuario entity) {
        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getAtivo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
