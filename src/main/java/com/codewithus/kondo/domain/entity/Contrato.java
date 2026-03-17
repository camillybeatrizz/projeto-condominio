package com.codewithus.kondo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contrato")
@Getter @Setter
public class Contrato extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    @ManyToOne
    private Fornecedor fornecedor;

    @ManyToOne
    private Condominio condominio;
}
