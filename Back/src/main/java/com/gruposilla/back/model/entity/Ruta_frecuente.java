package com.gruposilla.back.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ruta_frecuente")

public class Ruta_frecuente {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;

    @Lob
    private String coordenadas; // formato JSON

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


}
