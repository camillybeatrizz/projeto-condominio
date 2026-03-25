package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.StatusCobrancaEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "referencia_externa", unique = true)
    private String referenciaExterna;

    @Column(name = "url_pagamento_externo")
    private String urlPagamentoExterno;

    @Lob
    @Column(name = "pix_qr_code_base64")
    private String pixQrCodeBase64;

    @Column(name = "pix_copia_cola", length = 4000)
    private String pixCopiaCola;

    @Column(name = "pix_expiracao")
    private LocalDateTime pixExpiracao;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;
}
