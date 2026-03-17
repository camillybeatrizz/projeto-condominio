package com.codewithus.kondo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "bloco")
@Getter @Setter
public class Bloco {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;
}
