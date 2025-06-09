package com.gruposilla.back.algorithm.graph;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ConexionHabitacion implements Comparable<ConexionHabitacion> {
    private Habitacion a;
    private Habitacion b;
    private double distancia;

    public ConexionHabitacion(Habitacion a, Habitacion b) {
        this.a = a;
        this.b = b;
        this.distancia = calcularDistancia(a, b);
    }

    private double calcularDistancia(Habitacion h1, Habitacion h2) {
        int dx = h1.getCentroX() - h2.getCentroX();
        int dy = h1.getCentroY() - h2.getCentroY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Habitacion getA() { return a; }
    public Habitacion getB() { return b; }
    public double getDistancia() { return distancia; }

    @Override
    public int compareTo(ConexionHabitacion otra) {
        return Double.compare(this.distancia, otra.distancia);
    }
}