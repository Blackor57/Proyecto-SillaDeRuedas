package com.gruposilla.back.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MapaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String jsonMapa;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
