package com.gruposilla.back.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "habitacion")
//public class Habitacion {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private int x;
//    private int y;
//    private int ancho;
//    private int alto;
//    private String tipo;
//
//    @ManyToOne
//    @JoinColumn(name = "id_mapa")
//    private MapaEntity mapa;
//
//    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Mueble> muebles = new ArrayList<>();
//}
