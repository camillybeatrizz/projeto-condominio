package com.codewithus.kondo.mapper;

import com.codewithus.kondo.domain.entity.Endereco;
import com.codewithus.kondo.dto.endereco.EnderecoRequestDTO;
import com.codewithus.kondo.dto.endereco.EnderecoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoRequestDTO dto) {
        Endereco entity = new Endereco();
        entity.setLogradouro(dto.logradouro());
        entity.setNumero(dto.numero());
        entity.setComplemento(dto.complemento());
        entity.setBairro(dto.bairro());
        entity.setCidade(dto.cidade());
        entity.setEstado(dto.estado());
        entity.setCep(dto.cep());
        return entity;
    }

    public void updateEntity(Endereco entity, EnderecoRequestDTO dto) {
        entity.setLogradouro(dto.logradouro());
        entity.setNumero(dto.numero());
        entity.setComplemento(dto.complemento());
        entity.setBairro(dto.bairro());
        entity.setCidade(dto.cidade());
        entity.setEstado(dto.estado());
        entity.setCep(dto.cep());
    }

    public EnderecoResponseDTO toResponseDTO(Endereco entity) {
        return new EnderecoResponseDTO(
                entity.getId(),
                entity.getLogradouro(),
                entity.getNumero(),
                entity.getComplemento(),
                entity.getBairro(),
                entity.getCidade(),
                entity.getEstado(),
                entity.getCep(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
