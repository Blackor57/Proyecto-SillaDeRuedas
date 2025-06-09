package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Habitacion;

import java.util.ArrayList;
import java.util.List;

public class AsignadorDeMuebles {

    public void asignarMuebles(List<Habitacion> habitaciones) {
        for (Habitacion h : habitaciones) {
            List<String> muebles = new ArrayList<>();

            switch (h.getTipo()) {
                case "Sala" -> muebles.addAll(List.of("Sofá", "Mesa de centro", "TV"));
                case "Dormitorio" -> muebles.addAll(List.of("Cama", "Ropero", "Mesa de noche"));
                case "Cocina" -> muebles.addAll(List.of("Refrigerador", "Estufa", "Mesa de cocina"));
                case "Baño" -> muebles.addAll(List.of("Inodoro", "Lavamanos", "Ducha"));
                case "Recibidor" -> muebles.addAll(List.of("Perchero", "Espejo", "Zapatera"));
            }

            h.setMuebles(muebles);
        }
    }
}
