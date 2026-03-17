package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cobranca")
@Getter @Setter
public class Cobranca extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal valor;

    private LocalDate vencimento;

    @Enumerated(EnumType.STRING)
    private StatusCobrancaEnum status;

    private String competencia;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
}
