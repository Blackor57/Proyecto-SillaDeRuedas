package com.gruposilla.back.controller;

import com.gruposilla.back.algorithm.AEstrellaService;
import com.gruposilla.back.algorithm.graph.Nodo;
import com.gruposilla.back.model.DTO.CoordenadasRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ruta")
public class RutaController {

    @Autowired
    private AEstrellaService aEstrellaService;

    @PostMapping
    public List<Nodo> calcularRuta(@RequestBody CoordenadasRequest request){
        Nodo inicio = mapa.getNodoPorNombre(request.getInicio());
        Nodo fin = mapa.getNodoPorNombre(request.getFin());
        return aEstrellaService.encontrarRuta(inicio, fin);
    }
}
