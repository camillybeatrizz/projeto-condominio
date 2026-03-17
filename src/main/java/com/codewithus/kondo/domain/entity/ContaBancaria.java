package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.TipoContaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "conta_bancaria")
@Getter @Setter
public class ContaBancaria {

    @Id
    @GeneratedValue
    private UUID id;

    private String banco;
    private String agencia;
    private String conta;

    @Enumerated(EnumType.STRING)
    private TipoContaEnum tipo;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;


}
