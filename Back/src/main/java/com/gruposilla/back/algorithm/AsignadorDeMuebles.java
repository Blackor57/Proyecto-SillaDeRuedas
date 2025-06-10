package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Coordenada;
import com.gruposilla.back.algorithm.graph.Habitacion;
import com.gruposilla.back.algorithm.graph.Mueble;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AsignadorDeMuebles {

    private static final Map<String, int[]> tamañosMuebles = Map.ofEntries(
            Map.entry("Sofá", new int[]{2, 1}),
            Map.entry("Mesa de centro", new int[]{1, 1}),
            Map.entry("TV", new int[]{2, 1}),
            Map.entry("Cama", new int[]{2, 2}),
            Map.entry("Ropero", new int[]{1, 2}),
            Map.entry("Mesa de noche", new int[]{1, 1}),
            Map.entry("Refrigerador", new int[]{1, 1}),
            Map.entry("Estufa", new int[]{2, 1}),
            Map.entry("Mesa de cocina", new int[]{2, 2}),
            Map.entry("Inodoro", new int[]{1, 1}),
            Map.entry("Lavamanos", new int[]{1, 1}),
            Map.entry("Ducha", new int[]{1, 2}),
            Map.entry("Perchero", new int[]{1, 1}),
            Map.entry("Espejo", new int[]{1, 1}),
            Map.entry("Zapatera", new int[]{2, 1})
    );

    public void asignarMuebles(List<Habitacion> habitaciones) {
        for (Habitacion h : habitaciones) {
            List<Mueble> muebles = new ArrayList<>();
            List<Coordenada> colisiones = new ArrayList<>();
            Random random = new Random();

            List<String> tiposMuebles = switch (h.getTipo()) {
                case "Sala" -> List.of("Sofá", "Mesa de centro", "TV");
                case "Dormitorio" -> List.of("Cama", "Ropero", "Mesa de noche");
                case "Cocina" -> List.of("Refrigerador", "Estufa", "Mesa de cocina");
                case "Baño" -> List.of("Inodoro", "Lavamanos", "Ducha");
                case "Recibidor" -> List.of("Perchero", "Espejo", "Zapatera");
                default -> new ArrayList<>();
            };

            for (String tipo : tiposMuebles) {
                int[] tam = tamañosMuebles.getOrDefault(tipo, new int[]{1,1});
                int mAncho = tam[0];
                int mAlto = tam[1];

                boolean colocado = false;
                for (int intentos = 0; intentos < 20 && !colocado; intentos++) {
                    int fx = h.getX() + 1 + random.nextInt(Math.max(1, h.getAncho() - mAncho - 1));
                    int fy = h.getY() + 1 + random.nextInt(Math.max(1, h.getAlto() - mAlto - 1));

                    boolean colisiona = false;
                    List<Coordenada> espacioOcupado = new ArrayList<>();
                    for (int dx = 0; dx < mAncho; dx++) {
                        for (int dy = 0; dy < mAlto; dy++) {
                            Coordenada c = new Coordenada(fx + dx, fy + dy);
                            if (colisiones.contains(c)) {
                                colisiona = true;
                                break;
                            }
                            espacioOcupado.add(c);
                        }
                        if (colisiona) break;
                    }

                    if (!colisiona) {
                        muebles.add(new Mueble(tipo, new Coordenada(fx, fy), mAncho, mAlto));
                        colisiones.addAll(espacioOcupado);
                        colocado = true;
                    }
                }
            }

            h.setMuebles(muebles);
            h.setMueblesConColision(colisiones);
        }
    }
}

