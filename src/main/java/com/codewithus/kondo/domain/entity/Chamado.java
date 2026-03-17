package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.StatusChamadoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "chamado")
@Getter @Setter
public class Chamado {

    @Id
    @GeneratedValue
    private UUID id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusChamadoEnum status;

    private LocalDate dataAbertura;

    @ManyToOne
    private Unidade unidade;
}
