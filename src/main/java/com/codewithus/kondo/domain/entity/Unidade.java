package com.codewithus.kondo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "unidade")
@Getter @Setter
public class Unidade {

    @Id
    @GeneratedValue
    private UUID id;

    private String numero;
    private String andar;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "bloco_id")
    private Bloco bloco;
}
