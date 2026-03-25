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
@Table(name = "webhook_evento_processado")
@Getter
@Setter
public class WebhookEventoProcessado extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "provedor", nullable = false)
    private String provedor;

    @Column(name = "evento_externo_id", nullable = false, unique = true)
    private String eventoExternoId;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name = "payload_resumo", length = 1000)
    private String payloadResumo;
}
