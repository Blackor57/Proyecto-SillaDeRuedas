package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignadorTiposHabitacion {

    private int mapaAncho;
    private int mapaAlto;

    public void asignarTipos(List<Habitacion> habitaciones) {
        if (habitaciones.isEmpty()) return;

        int maxX = habitaciones.stream().mapToInt(h -> h.getX() + h.getAncho()).max().orElse(0);
        int maxY = habitaciones.stream().mapToInt(h -> h.getY() + h.getAlto()).max().orElse(0);

        // Ordenar por tamaño (grande a pequeño)
        habitaciones.sort(Comparator.comparingInt(h -> -(h.getAncho() * h.getAlto())));

        Set<Habitacion> asignadas = new HashSet<>();

        // Sala: más grande
        Habitacion sala = habitaciones.get(0);
        sala.setTipo("Sala");
        asignadas.add(sala);

        // Cocina: grande, no adyacente a baño
        for (Habitacion h : habitaciones) {
            if (!asignadas.contains(h) && !esEsquina(h, maxX, maxY) && !esAdyacenteATipo(h, "Baño", habitaciones)) {
                h.setTipo("Cocina");
                asignadas.add(h);
                break;
            }
        }

        // Recibidor: más al centro
        Habitacion centro = obtenerHabitacionCentral(habitaciones);
        if (!asignadas.contains(centro)) {
            centro.setTipo("Recibidor");
            asignadas.add(centro);
        }

        // Baños: solo en esquinas
        int baños = 0;
        for (Habitacion h : habitaciones) {
            if (!asignadas.contains(h) && esEsquina(h, maxX, maxY)) {
                h.setTipo("Baño");
                asignadas.add(h);
                baños++;
                if (baños == 2) break;
            }
        }

        // Dormitorios: no en esquina, no juntos, uno debe estar en esquina
        int dormitorios = 0;
        for (Habitacion h : habitaciones) {
            if (asignadas.contains(h)) continue;
            if (dormitorios == 0 && esEsquina(h, maxX, maxY)) {
                h.setTipo("Dormitorio");
                asignadas.add(h);
                dormitorios++;
            } else if (!esAdyacenteATipo(h, "Dormitorio", habitaciones) &&
                    !esAdyacenteATipo(h, "Cocina", habitaciones) &&
                    !esAdyacenteATipo(h, "Sala", habitaciones)) {
                h.setTipo("Dormitorio");
                asignadas.add(h);
                dormitorios++;
                if (dormitorios == 2) break;
            }
        }

        // ✅ Eliminar habitaciones no asignadas (antes se marcaban como "Pasillo")
        habitaciones.removeIf(h -> !asignadas.contains(h));
    }


    private boolean esEsquina(Habitacion h, int maxX, int maxY) {
        int margen = 5;
        boolean esquinaIzq = h.getX() <= margen;
        boolean esquinaDer = h.getX() + h.getAncho() >= maxX - margen;
        boolean esquinaArriba = h.getY() <= margen;
        boolean esquinaAbajo = h.getY() + h.getAlto() >= maxY - margen;

        return (esquinaIzq || esquinaDer) && (esquinaArriba || esquinaAbajo);
    }

    private boolean esAdyacenteATipo(Habitacion h, String tipo, List<Habitacion> habitaciones) {
        for (Habitacion otra : habitaciones) {
            if (tipo.equals(otra.getTipo()) && esAdyacente(h, otra)) {
                return true;
            }
        }
        return false;
    }

    private boolean esAdyacente(Habitacion a, Habitacion b) {
        boolean horizontal = a.getY() + a.getAlto() > b.getY() && a.getY() < b.getY() + b.getAlto();
        boolean vertical = a.getX() + a.getAncho() > b.getX() && a.getX() < b.getX() + b.getAncho();

        return (Math.abs(a.getX() + a.getAncho() - b.getX()) <= 1 && horizontal) || // derecha
                (Math.abs(b.getX() + b.getAncho() - a.getX()) <= 1 && horizontal) || // izquierda
                (Math.abs(a.getY() + a.getAlto() - b.getY()) <= 1 && vertical) ||    // abajo
                (Math.abs(b.getY() + b.getAlto() - a.getY()) <= 1 && vertical);      // arriba
    }

    private Habitacion obtenerHabitacionCentral(List<Habitacion> habitaciones) {
        int centroX = habitaciones.stream().mapToInt(Habitacion::getCentroX).sum() / habitaciones.size();
        int centroY = habitaciones.stream().mapToInt(Habitacion::getCentroY).sum() / habitaciones.size();

        return habitaciones.stream()
                .min(Comparator.comparingInt(h -> distancia(h.getCentroX(), h.getCentroY(), centroX, centroY)))
                .orElse(habitaciones.get(0));
    }

    private int distancia(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }


    public void imprimirResumenHabitaciones(List<Habitacion> habitaciones) {
        Map<String, Long> conteo = habitaciones.stream()
                .collect(Collectors.groupingBy(Habitacion::getTipo, Collectors.counting()));

        System.out.println("Resumen de tipos de habitación:");
        conteo.forEach((tipo, cantidad) -> System.out.println(tipo + ": " + cantidad));
    }
}