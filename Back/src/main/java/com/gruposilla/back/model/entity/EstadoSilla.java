package com.gruposilla.back.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
@Table(name = "estado_silla")

public class EstadoSilla {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private Integer bateria;
    private String estadoSistema;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


}
