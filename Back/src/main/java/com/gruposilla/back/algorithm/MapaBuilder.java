package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Arista;
import com.gruposilla.back.algorithm.graph.Nodo;
import com.gruposilla.back.model.DTO.AristaDTO;
import com.gruposilla.back.model.DTO.NodoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapaBuilder {


    public Map<String, Nodo> construirMapaDesdeJson(List<NodoDTO> nodosDTO, List<AristaDTO> aristasDTO) {
        Map<String, Nodo> mapa = new HashMap<>();

        // Crear los nodos
        for (NodoDTO dto : nodosDTO) {
            Nodo nodo = new Nodo();
            nodo.setId(-1); // ya no usamos ID de DB, puedes dejarlo como -1 o un contador
            nodo.setX(dto.getX());
            nodo.setY(dto.getY());
            nodo.setConexiones(new ArrayList<>());
            nodo.setG(0);
            nodo.setH(0);
            nodo.setF(0);
            nodo.setPadre(null);

            mapa.put(dto.getIdentificador(), nodo);
        }

        // Crear las aristas
        for (AristaDTO aristaDTO : aristasDTO) {
            Nodo origen = mapa.get(aristaDTO.getOrigenId());
            Nodo destino = mapa.get(aristaDTO.getDestinoId());

            if (origen != null && destino != null) {
                origen.getConexiones().add(new Arista(destino, aristaDTO.getCosto()));
            }
        }

        return mapa;
    }
}

