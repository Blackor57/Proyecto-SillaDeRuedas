package com.gruposilla.back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruposilla.back.algorithm.AEstrellaService;
import com.gruposilla.back.algorithm.BSPGenerator;
import com.gruposilla.back.algorithm.MapaBuilder;
import com.gruposilla.back.algorithm.graph.Coordenada;
import com.gruposilla.back.algorithm.graph.Habitacion;
import com.gruposilla.back.algorithm.graph.Nodo;
import com.gruposilla.back.model.DTO.*;
import com.gruposilla.back.model.entity.MapaEntity;
import com.gruposilla.back.model.entity.Usuario;
import com.gruposilla.back.repository.MapaRepository;
import com.gruposilla.back.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ruta")
public class RutaController {

    private final AEstrellaService aEstrellaService;
    private final MapaBuilder mapaBuilder;
    private final MapaRepository mapaRepository;
    private final UsuarioRepository usuarioRepository;
    private List<Habitacion> habitacionesGeneradas = new ArrayList<>();

    public RutaController(
            AEstrellaService aEstrellaService,
            MapaBuilder mapaBuilder,
            MapaRepository mapaRepository, UsuarioRepository usuarioRepository) {
        this.aEstrellaService = aEstrellaService;
        this.mapaBuilder = mapaBuilder;
        this.mapaRepository = mapaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public List<CoordenadaDTO> calcularRuta(@RequestBody CoordenadasRequest request) {
        MapaEntity mapaGuardado = mapaRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay mapa guardado"));

        MapaRequest mapaRequest;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapaRequest = mapper.readValue(mapaGuardado.getJsonMapa(), MapaRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el JSON del mapa");
        }

        Map<String, Nodo> mapa = mapaBuilder.construirMapaDesdeJson(mapaRequest.getNodos(), mapaRequest.getAristas());

        // Buscar nodos por coordenadas
        Nodo inicio = mapa.values().stream()
                .filter(n -> n.getX() == request.getInicioX() && n.getY() == request.getInicioY())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nodo de inicio no encontrado"));

        Nodo fin = mapa.values().stream()
                .filter(n -> n.getX() == request.getFinX() && n.getY() == request.getFinY())
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nodo de fin no encontrado"));

        List<Nodo> ruta = aEstrellaService.encontrarRuta(inicio, fin);

        return ruta.stream()
                .map(n -> new CoordenadaDTO(n.getX(), n.getY()))
                .collect(Collectors.toList());
    }

    @PostMapping("/guardar-mapa")
    public ResponseEntity<?> guardarMapaComoJson(@RequestBody MapaRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
            }

            // Método 1: si usas UserDetails (recomendado)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            // Buscar usuario por correo
            Usuario usuario = usuarioRepository.findByCorreo(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            ObjectMapper mapper = new ObjectMapper();
            String jsonMapa = mapper.writeValueAsString(request);

            MapaEntity mapaEntity = new MapaEntity();
            mapaEntity.setJsonMapa(jsonMapa);
            mapaEntity.setUsuario(usuario);
            mapaRepository.save(mapaEntity);

            return ResponseEntity.ok("Mapa guardado como JSON");

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al convertir el mapa a JSON");
        }
    }

    private void guardarMapaInternamente(MapaRequest request, Usuario usuario) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonMapa = mapper.writeValueAsString(request);

            MapaEntity mapaEntity = new MapaEntity();
            mapaEntity.setJsonMapa(jsonMapa);
            mapaEntity.setUsuario(usuario);
            mapaRepository.save(mapaEntity);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al guardar el mapa generado");
        }
    }

    @PostMapping("/generar-bsp")
    public ResponseEntity<?> generarMapaBSP() {
        BSPGenerator generator = new BSPGenerator();
        MapaRequest mapa = generator.generarMapa(30, 20);

        this.habitacionesGeneradas = generator.getHabitaciones();

        // Obtener usuario
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String email = userDetails.getUsername();
        Usuario usuario = usuarioRepository.findByCorreo(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Guardar usando método auxiliar
        guardarMapaInternamente(mapa, usuario);

        return ResponseEntity.ok(mapa);
    }

    @GetMapping("/mapa/centro")
    public ResponseEntity<Coordenada> getCentroPorTipo(@RequestParam String tipo) {
        System.out.println("🧭 Buscando habitación tipo: " + tipo);
        System.out.println("Habitaciones disponibles:");

        for (Habitacion h : habitacionesGeneradas) {
            System.out.println("- " + h.getTipo());
        }

        Optional<Habitacion> hab = habitacionesGeneradas.stream()
                .filter(h -> h.getTipo() != null && h.getTipo().equalsIgnoreCase(tipo))
                .findFirst();

        if (hab.isPresent()) {
            Coordenada centro = new Coordenada(hab.get().getCentroX(), hab.get().getCentroY());
            return ResponseEntity.ok(centro);
        } else {
            System.out.println("❌ No se encontró habitación del tipo: " + tipo);
            return ResponseEntity.notFound().build();
        }
    }

}
