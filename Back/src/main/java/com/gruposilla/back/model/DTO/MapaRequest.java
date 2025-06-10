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
    private List<MuebleDTO> muebles = new ArrayList<>();
    private List<HabitacionDTO> habitaciones = new ArrayList<>();

    public MapaRequest(List<NodoDTO> nodos, List<AristaDTO> aristas, List<Coordenada> muros) {
        this.nodos = nodos;
        this.aristas = aristas;
        this.muros = muros;
    }
}
