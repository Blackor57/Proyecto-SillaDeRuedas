package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignadorTiposHabitacion {

    private int mapaAncho;
    private int mapaAlto;

    public void asignarTipos(List<Habitacion> habitaciones) {
        // Ordenar habitaciones por tamaño descendente (más grandes primero)
        habitaciones.sort(Comparator.comparingInt(h -> -(h.getAncho() * h.getAlto())));

        // Cantidades deseadas por tipo
        Map<String, Integer> cantidadesDeseadas = new LinkedHashMap<>();
        cantidadesDeseadas.put("Sala", 1);
        cantidadesDeseadas.put("Dormitorio", 2);
        cantidadesDeseadas.put("Baño", 2);
        cantidadesDeseadas.put("Cocina", 1);
        cantidadesDeseadas.put("Recibidor", 1);

        int index = 0;

        for (Map.Entry<String, Integer> entry : cantidadesDeseadas.entrySet()) {
            String tipo = entry.getKey();
            int cantidad = entry.getValue();

            for (int i = 0; i < cantidad && index < habitaciones.size(); i++, index++) {
                habitaciones.get(index).setTipo(tipo);
            }
        }

        // Las habitaciones restantes se etiquetan como "Pasillo"
        while (index < habitaciones.size()) {
            habitaciones.get(index++).setTipo("Pasillo");
        }

    }

    public void imprimirResumenHabitaciones(List<Habitacion> habitaciones) {
        Map<String, Long> conteo = habitaciones.stream()
                .collect(Collectors.groupingBy(Habitacion::getTipo, Collectors.counting()));

        System.out.println("Resumen de tipos de habitación:");
        conteo.forEach((tipo, cantidad) -> System.out.println(tipo + ": " + cantidad));
    }
}