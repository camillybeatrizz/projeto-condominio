package com.codewithus.kondo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter @Setter
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;
    private String telefone;
    private Boolean ativo = true;
}
