package com.codewithus.kondo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "fornecedor")
@Getter @Setter
public class Fornecedor extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String nome;
    private String cnpj;
    private String telefone;
}
