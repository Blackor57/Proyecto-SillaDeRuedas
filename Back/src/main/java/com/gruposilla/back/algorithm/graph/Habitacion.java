package com.gruposilla.back.algorithm.graph;


import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Data
public class Habitacion {
    private int x;
    private int y;
    private int ancho;
    private int alto;
    private String tipo;
    private List<Mueble> muebles = new ArrayList<>();
    private List<Coordenada> mueblesConColision = new ArrayList<>();

    public Habitacion(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.tipo = null; // tipo aún no asignado
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habitacion that = (Habitacion) o;
        return x == that.x && y == that.y && ancho == that.ancho && alto == that.alto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, ancho, alto);
    }
}