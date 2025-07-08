package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.ConexionHabitacion;
import com.gruposilla.back.algorithm.graph.Coordenada;
import com.gruposilla.back.algorithm.graph.Habitacion;
import com.gruposilla.back.algorithm.graph.Mueble;
import com.gruposilla.back.model.DTO.AristaDTO;
import com.gruposilla.back.model.DTO.MapaRequest;
import com.gruposilla.back.model.DTO.NodoDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BSPGenerator {

    private final int MIN_TAMANIO = 6;
    private List<Habitacion> habitaciones;
    private boolean usarPasilloCentral = false      ; // Configuración para elegir el tipo de pasillos
    private int mapaAncho;
    private int mapaAlto;

    public MapaRequest generarMapa(int ancho, int alto) {
        this.mapaAncho = ancho;
        this.mapaAlto = alto;
        habitaciones = new ArrayList<>();
        NodoBSP raiz = dividir(new NodoBSP(0, 0, ancho, alto));
        crearHabitaciones(raiz);

        AsignadorTiposHabitacion asignador = new AsignadorTiposHabitacion(ancho, alto);
        asignador.asignarTipos(habitaciones);
        asignador.imprimirResumenHabitaciones(habitaciones);

        AsignadorDeMuebles mueblador = new AsignadorDeMuebles();
        mueblador.asignarMuebles(habitaciones);

        conectarHabitacionesConMST();

        imprimirMapaVisual(ancho, alto);

        return convertirHabitacionesAMapa(ancho, alto);

    }

    public List<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    private boolean esEsquina(Habitacion hab) {
        return (hab.getX() <= 1 || hab.getX() + hab.getAncho() >= mapaAncho - 1) &&
                (hab.getY() <= 1 || hab.getY() + hab.getAlto() >= mapaAlto - 1);
    }

    private int ajustarDesdeEsquina(int coord, int inicio, int tamanio) {
        if (coord == inicio) {
            return coord + 1;
        } else if (coord == inicio + tamanio - 1) {
            return coord - 1;
        }
        return coord;
    }


    private void crearPasilloEntreHabitaciones(Habitacion h1, Habitacion h2) {
        int x1 = h1.getCentroX();
        int y1 = h1.getCentroY();
        int x2 = h2.getCentroX();
        int y2 = h2.getCentroY();

        // Verificar si estamos en una esquina
        boolean esquina1 = esEsquina(h1);
        boolean esquina2 = esEsquina(h2);

        if (esquina1 || esquina2) {
            // Evitar conectar directamente desde la esquina
            if (esquina1) {
                x1 = ajustarDesdeEsquina(x1, h1.getX(), h1.getAncho());
                y1 = ajustarDesdeEsquina(y1, h1.getY(), h1.getAlto());
            }
            if (esquina2) {
                x2 = ajustarDesdeEsquina(x2, h2.getX(), h2.getAncho());
                y2 = ajustarDesdeEsquina(y2, h2.getY(), h2.getAlto());
            }
        }

        if (x1 == x2 || y1 == y2) {
            crearPasilloHorizontal(x1, x2, y1);
            crearPasilloVertical(y1, y2, x2);
        } else {
            if (new Random().nextBoolean()) {
                crearPasilloHorizontal(x1, x2, y1);
                crearPasilloVertical(y1, y2, x2);
            } else {
                crearPasilloVertical(y1, y2, x1);
                crearPasilloHorizontal(x1, x2, y2);
            }
        }
    }




    private void crearPasilloHorizontal(int x1, int x2, int y) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            habitaciones.add(new Habitacion(x, y, 1, 1, "Pasillo"));
        }
    }

    private void crearPasilloVertical(int y1, int y2, int x) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            habitaciones.add(new Habitacion(x, y, 1, 1, "Pasillo"));
        }
    }

    private NodoBSP dividir(NodoBSP nodo) {
        if (nodo.ancho < MIN_TAMANIO * 2 && nodo.alto < MIN_TAMANIO * 2) {
            return nodo;
        }

        boolean dividirVertical = nodo.ancho > nodo.alto;
        int max = (dividirVertical ? nodo.ancho : nodo.alto) - MIN_TAMANIO;
        if (max <= MIN_TAMANIO) return nodo;

        int corte = MIN_TAMANIO + new Random().nextInt(max - MIN_TAMANIO + 1);

        if (dividirVertical) {
            nodo.izquierda = dividir(new NodoBSP(nodo.x, nodo.y, corte, nodo.alto));
            nodo.derecha = dividir(new NodoBSP(nodo.x + corte, nodo.y, nodo.ancho - corte, nodo.alto));
        } else {
            nodo.izquierda = dividir(new NodoBSP(nodo.x, nodo.y, nodo.ancho, corte));
            nodo.derecha = dividir(new NodoBSP(nodo.x, nodo.y + corte, nodo.ancho, nodo.alto - corte));
        }

        return nodo;
    }

    private void crearHabitaciones(NodoBSP nodo) {
        if (nodo.izquierda == null && nodo.derecha == null) {
            int roomX = nodo.x;
            int roomY = nodo.y;
            int roomW = nodo.ancho;
            int roomH = nodo.alto;

            if (roomW > 4) {
                roomX += 1;
                roomW = Math.max(1, roomW - 2);
            }
            if (roomH > 4) {
                roomY += 1;
                roomH = Math.max(1, roomH - 2);
            }

            habitaciones.add(new Habitacion(roomX, roomY, roomW, roomH));
        } else {
            if (nodo.izquierda != null) crearHabitaciones(nodo.izquierda);
            if (nodo.derecha != null) crearHabitaciones(nodo.derecha);
        }
    }

    public void conectarHabitacionesConMST() {
        List<ConexionHabitacion> conexiones = GenerarMST.conectarHabitaciones(habitaciones);
        for (ConexionHabitacion conexion : conexiones) {
            crearPasilloEntreHabitaciones(conexion.getA(), conexion.getB());
        }
    }

    private List<Coordenada> calcularMuros(boolean[][] grid) {
        List<Coordenada> muros = new ArrayList<>();
        int ancho = grid.length;
        int alto = grid[0].length;

        int[][] direcciones = {
                {0, 1}, {1, 0}, {0, -1}, {-1, 0}
        };

        Set<String> agregados = new HashSet<>();

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                if (grid[x][y]) {
                    for (int[] dir : direcciones) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];

                        // Solo agregamos como muro si está dentro del mapa
                        if (nx >= 0 && nx < ancho && ny >= 0 && ny < alto && !grid[nx][ny]) {
                            String key = nx + "," + ny;
                            if (agregados.add(key)) {
                                muros.add(new Coordenada(nx, ny));
                            }
                        }
                    }
                }
            }
        }

        return muros;
    }

    private MapaRequest convertirHabitacionesAMapa(int ancho, int alto) {
        boolean[][] grid = new boolean[ancho][alto];
        List<NodoDTO> nodos = new ArrayList<>();
        List<AristaDTO> aristas = new ArrayList<>();
        Map<String, String> nodoIds = new HashMap<>();

        int idCounter = 0;

        // Marcar habitaciones y pasillos en el grid
        for (Habitacion h : habitaciones) {
            for (int x = h.getX(); x < h.getX() + h.getAncho(); x++) {
                for (int y = h.getY(); y < h.getY() + h.getAlto(); y++) {
                    if (x >= 0 && x < ancho && y >= 0 && y < alto) {
                        grid[x][y] = true;
                    }
                }
            }
        }

        // Marcar celdas ocupadas por muebles con colisión como no caminables
        for (Habitacion h : habitaciones) {
            if (h.getMueblesConColision() != null) {
                for (Coordenada c : h.getMueblesConColision()) {
                    if (c.getX() >= 0 && c.getX() < ancho && c.getY() >= 0 && c.getY() < alto) {
                        grid[c.getX()][c.getY()] = false; // Bloqueamos esta celda
                    }
                }
            }
        }

        // Crear nodos válidos
        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                if (grid[x][y]) {
                    String id = "n" + idCounter++;
                    nodoIds.put(x + "," + y, id);
                    nodos.add(new NodoDTO(id, x, y));
                }
            }
        }

        // Crear aristas entre vecinos válidos (4-direcciones)
        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        for (NodoDTO nodo : nodos) {
            int x = (int) Math.round(nodo.getX());
            int y = (int) Math.round(nodo.getY());
            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];
                String neighborKey = nx + "," + ny;
                if (gridValido(grid, nx, ny) && nodoIds.containsKey(neighborKey)) {
                    aristas.add(new AristaDTO(nodo.getIdentificador(), nodoIds.get(neighborKey), 1));
                }
            }
        }

        List<Coordenada> muros = calcularMuros(grid);

        return new MapaRequest(nodos, aristas, muros);
    }

    private boolean gridValido(boolean[][] grid, int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length;
    }

    private static class NodoBSP {
        int x, y, ancho, alto;
        NodoBSP izquierda, derecha;

        NodoBSP(int x, int y, int ancho, int alto) {
            this.x = x;
            this.y = y;
            this.ancho = ancho;
            this.alto = alto;
        }
    }



    public void imprimirMapaVisual(int ancho, int alto) {
        char[][] grid = new char[ancho][alto];

        // Primero dibujar habitaciones
        for (Habitacion h : habitaciones) {
            char simbolo = switch (h.getTipo()) {
                case "Sala" -> 'S';
                case "Dormitorio" -> 'D';
                case "Pasillo" -> 'P';
                case "Cocina" -> 'C';
                case "Baño" -> 'B';
                case "Recibidor" -> 'R';
                default -> '?';
            };

            for (int x = h.getX(); x < h.getX() + h.getAncho(); x++) {
                for (int y = h.getY(); y < h.getY() + h.getAlto(); y++) {
                    if (x >= 0 && x < ancho && y >= 0 && y < alto) {
                        grid[x][y] = simbolo;
                    }
                }
            }

            // Luego dibujar los muebles encima
            for (Mueble m : h.getMuebles()) {
                for (int dx = 0; dx < m.getAncho(); dx++) {
                    for (int dy = 0; dy < m.getAlto(); dy++) {
                        int x = m.getPosicion().getX() + dx;
                        int y = m.getPosicion().getY() + dy;
                        if (x >= 0 && x < ancho && y >= 0 && y < alto) {
                            grid[x][y] = obtenerSimboloMueble(m.getTipo());
                        }
                    }
                }
            }
        }

        // Imprimir
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                System.out.print(grid[x][y] == 0 ? ' ' : grid[x][y]);
            }
            System.out.println();
        }
    }



    // Método auxiliar para asignar símbolo a cada mueble
    private char obtenerSimboloMueble(String tipo) {
        return switch (tipo) {
            case "Sofá" -> 's';
            case "Mesa de centro", "Mesa de noche", "Mesa de cocina" -> 'm';
            case "TV" -> 't';
            case "Cama" -> 'c';
            case "Ropero" -> 'r';
            case "Refrigerador" -> 'f';
            case "Estufa" -> 'e';
            case "Inodoro" -> 'i';
            case "Lavamanos" -> 'l';
            case "Ducha" -> 'd';
            case "Perchero" -> 'p';
            case "Espejo" -> 'v';
            case "Zapatera" -> 'z';
            default -> '*';
        };
    }
}