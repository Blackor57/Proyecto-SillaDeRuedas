package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.ConexionHabitacion;
import com.gruposilla.back.algorithm.graph.DisjointSet;
import com.gruposilla.back.algorithm.graph.Habitacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerarMST {

    public static List<ConexionHabitacion> conectarHabitaciones(List<Habitacion> habitaciones) {
        List<ConexionHabitacion> conexiones = new ArrayList<>();

        // Genera todas las posibles conexiones entre habitaciones
        for (int i = 0; i < habitaciones.size(); i++) {
            for (int j = i + 1; j < habitaciones.size(); j++) {
                conexiones.add(new ConexionHabitacion(habitaciones.get(i), habitaciones.get(j)));
            }
        }

        // Ordena por distancia (Kruskal)
        Collections.sort(conexiones);

        // Kruskal usando DisjointSet
        DisjointSet<Habitacion> ds = new DisjointSet<>();
        ds.makeSet(habitaciones);

        List<ConexionHabitacion> pasillosFinales = new ArrayList<>();

        for (ConexionHabitacion conexion : conexiones) {
            if (!ds.connected(conexion.getA(), conexion.getB())) {
                ds.union(conexion.getA(), conexion.getB());
                pasillosFinales.add(conexion);
            }
        }

        return pasillosFinales;
    }
}