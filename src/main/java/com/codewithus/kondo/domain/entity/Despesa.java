package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.CategoriaDespesaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "despesa")
@Getter @Setter
public class Despesa {

    @Id
    @GeneratedValue
    private UUID id;

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private CategoriaDespesaEnum categoria;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;
}
