package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.FormaPagamentoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
@Getter @Setter
public class Pagamento extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal valor;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    private FormaPagamentoEnum forma;

    @Column(unique = true)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "cobranca_id")
    private Cobranca cobranca;
}
