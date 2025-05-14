package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Arista;
import com.gruposilla.back.algorithm.graph.Nodo;
import com.gruposilla.back.model.entity.AristaEntity;
import com.gruposilla.back.model.entity.NodoEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapaBuilder {

    public Map<Long, Nodo> construirMapa(List<NodoEntity> nodosBD, List<AristaEntity> aristasBD) {
        Map<Long, Nodo> mapa = new HashMap<>();

        for (NodoEntity ne : nodosBD) {
            Nodo nodo = new Nodo(ne.getId(), ne.getX(), ne.getY(), new ArrayList<>(), 0, 0, 0, null);
            mapa.put(ne.getId(), nodo);
        }

        for (AristaEntity ae : aristasBD) {
            Nodo origen = mapa.get(ae.getOrigen().getId());
            Nodo destino = mapa.get(ae.getDestino().getId());
            origen.getConexiones().add(new Arista(destino, ae.getCosto()));
        }

        return mapa;
    }
}
