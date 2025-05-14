package com.gruposilla.back.controller;

import com.gruposilla.back.algorithm.AEstrellaService;
import com.gruposilla.back.algorithm.MapaBuilder;
import com.gruposilla.back.algorithm.graph.Nodo;
import com.gruposilla.back.model.DTO.AristaDTO;
import com.gruposilla.back.model.DTO.CoordenadasRequest;
import com.gruposilla.back.model.DTO.MapaRequest;
import com.gruposilla.back.model.DTO.NodoDTO;
import com.gruposilla.back.model.entity.AristaEntity;
import com.gruposilla.back.model.entity.NodoEntity;
import com.gruposilla.back.model.entity.Usuario;
import com.gruposilla.back.repository.AristaRepository;
import com.gruposilla.back.repository.NodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ruta")
public class RutaController {

    private final AEstrellaService aEstrellaService;
    private final MapaBuilder mapaBuilder;
    private final NodoRepository nodoRepository;
    private final AristaRepository aristaRepository;

    public RutaController(
            AEstrellaService aEstrellaService,
            MapaBuilder mapaBuilder,
            NodoRepository nodoRepository,
            AristaRepository aristaRepository
    ) {
        this.aEstrellaService = aEstrellaService;
        this.mapaBuilder = mapaBuilder;
        this.nodoRepository = nodoRepository;
        this.aristaRepository = aristaRepository;
    }

    @PostMapping
    public List<Nodo> calcularRuta(@RequestBody CoordenadasRequest request) {
        List<NodoEntity> nodosBD = nodoRepository.findAll();
        List<AristaEntity> aristasBD = aristaRepository.findAll();
        Map<Long, Nodo> mapa = mapaBuilder.construirMapa(nodosBD, aristasBD);

        // Buscar nodos por coordenadas exactas
        NodoEntity nodoInicio = nodoRepository.findByXAndY(request.getInicioX(), request.getInicioY());
        NodoEntity nodoFin = nodoRepository.findByXAndY(request.getFinX(), request.getFinY());

        if (nodoInicio == null || nodoFin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nodos no encontrados");
        }

        Nodo inicio = mapa.get(nodoInicio.getId());
        Nodo fin = mapa.get(nodoFin.getId());

        return aEstrellaService.encontrarRuta(inicio, fin);
    }

    @PostMapping("/guardar-mapa")
    public ResponseEntity<?> guardarMapa(@RequestBody MapaRequest request) {
        // Guardar nodos primero
        Map<String, NodoEntity> nodoMap = new HashMap<>();
        for (NodoDTO nodoDTO : request.getNodos()) {
            NodoEntity nodo = new NodoEntity();
            nodo.setX(nodoDTO.getX());
            nodo.setY(nodoDTO.getY());
            nodoRepository.save(nodo);
            nodoMap.put(nodoDTO.getIdentificador(), nodo);
        }

        // Luego guardar aristas
        for (AristaDTO aristaDTO : request.getAristas()) {
            NodoEntity origen = nodoMap.get(aristaDTO.getOrigenId());
            NodoEntity destino = nodoMap.get(aristaDTO.getDestinoId());

            AristaEntity arista = new AristaEntity();
            arista.setOrigen(origen);
            arista.setDestino(destino);
            arista.setCosto(aristaDTO.getCosto());
            aristaRepository.save(arista);
        }

        return ResponseEntity.ok("Mapa guardado exitosamente");
    }
}
