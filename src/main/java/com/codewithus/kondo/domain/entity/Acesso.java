package com.codewithus.kondo.domain.entity;

import com.codewithus.kondo.domain.enums.PerfilEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "acesso")
@Getter @Setter
public class Acesso {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private PerfilEnum perfil;
}
