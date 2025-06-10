package com.gruposilla.back.algorithm.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Mueble {
    private String tipo;
    private Coordenada posicion;
    private int ancho = 1;
    private int alto = 1;

}