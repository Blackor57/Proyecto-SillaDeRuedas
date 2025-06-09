package com.gruposilla.back.model.DTO;

import com.gruposilla.back.algorithm.graph.Coordenada;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapaRequest {
    private List<NodoDTO> nodos;
    private List<AristaDTO> aristas;
    private List<Coordenada> muros = new ArrayList<>();

    // Constructor con solo nodos y aristas
    public MapaRequest(List<NodoDTO> nodos, List<AristaDTO> aristas) {
        this.nodos = nodos;
        this.aristas = aristas;
        this.muros = new ArrayList<>();
    }
}
