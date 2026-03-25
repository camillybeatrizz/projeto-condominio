package com.codewithus.kondo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "condominio")
@Getter @Setter
public class Condominio extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;
    private String cnpj;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    private LocalDateTime deletedAt;
    private String deletedBy;
}
