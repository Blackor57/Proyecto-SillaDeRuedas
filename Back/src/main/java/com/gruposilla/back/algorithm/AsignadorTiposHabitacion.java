package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignadorTiposHabitacion {

    private int mapaAncho;
    private int mapaAlto;

    public void asignarTipos(List<Habitacion> habitaciones) {
        for (Habitacion h : habitaciones) {
            String tipo = determinarTipo(h);
            h.setTipo(tipo);
        }
    }

    private String determinarTipo(Habitacion h) {
        int ancho = h.getAncho();
        int alto = h.getAlto();
        int x = h.getX();
        int y = h.getY();

        // Sala si es muy grande
        if (ancho >= 8 && alto >= 8) {
            return "Sala";
        }

        // Dormitorio si está en esquina
        if (esEnEsquina(x, y)) {
            return "Dormitorio";
        }

        // Recibidor cerca de entrada (asumimos entrada en (0,0) o cerca)
        if (estaCercaDeEntrada(x, y)) {
            return "Recibidor";
        }

        // Cocina o baño aleatorio para el resto
        return Math.random() < 0.5 ? "Cocina" : "Baño";
    }

    private boolean esEnEsquina(int x, int y) {
        return (x <= 1 && y <= 1) || // esquina superior izquierda
                (x >= mapaAncho - 2 && y <= 1) || // esquina superior derecha
                (x <= 1 && y >= mapaAlto - 2) || // esquina inferior izquierda
                (x >= mapaAncho - 2 && y >= mapaAlto - 2); // esquina inferior derecha
    }

    private boolean estaCercaDeEntrada(int x, int y) {
        // Consideramos entrada cerca de (0,0)
        return x <= 3 && y <= 3;
    }
}
