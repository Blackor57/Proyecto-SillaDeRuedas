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
@Table(name = "comando")

public class Comando {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComando;

    private String tipo;      // Ej: Avanzar, Detenerse, Girar
    private String resultado; // Éxito, Error, Obstáculo

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
