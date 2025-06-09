package com.gruposilla.back.algorithm.graph;


import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data

public class Habitacion {
    private int x;
    private int y;
    private int ancho;
    private int alto;
    private String tipo;
    private List<String> muebles = new ArrayList<>();

    public Habitacion(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = null; // tipo no asignado aún
    }

    public Habitacion(int x, int y, int ancho, int alto, String tipo) {
        this(x, y, ancho, alto);
        this.tipo = tipo;
    }

    public int getCentroX() {
        return x + ancho / 2;
    }

    public int getCentroY() {
        return y + alto / 2;
    }

    public boolean contiene(int px, int py) {
        return px >= x && px < x + ancho && py >= y && py < y + alto;
    }
}
