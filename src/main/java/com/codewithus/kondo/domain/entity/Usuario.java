package com.codewithus.kondo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter @Setter
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(name = "asaas_customer_id", unique = true)
    private String asaasCustomerId;

    private String telefone;
    private Boolean ativo = true;
}
