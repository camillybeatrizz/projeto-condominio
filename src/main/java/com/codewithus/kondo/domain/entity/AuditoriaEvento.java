package com.codewithus.kondo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "auditoria_evento")
@Getter
@Setter
public class AuditoriaEvento extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name = "entidade", nullable = false)
    private String entidade;

    @Column(name = "entidade_id")
    private String entidadeId;

    @Column(name = "ator", nullable = false)
    private String ator;

    @Column(name = "detalhe", length = 1000, nullable = false)
    private String detalhe;
}
