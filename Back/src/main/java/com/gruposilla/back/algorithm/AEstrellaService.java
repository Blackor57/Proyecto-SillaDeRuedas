package com.gruposilla.back.algorithm;

import com.gruposilla.back.algorithm.graph.Arista;
import com.gruposilla.back.algorithm.graph.Nodo;
import com.gruposilla.back.services.AEstrellaIFA;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AEstrellaService implements AEstrellaIFA {

    @Override
    public List<Nodo> encontrarRuta(Nodo incio, Nodo fin){
        List<Nodo> abierta = new ArrayList<>();
        List<Nodo> cerrada = new ArrayList<>();

        incio.setG(0);
        abierta.add(incio);

        while (!abierta.isEmpty()){
            Nodo actual = obtenerNodoConMenorF(abierta);

            if (actual.equals(fin)){
                return reconstruirRuta(actual);
            }

            abierta.remove(actual);
            cerrada.add(actual);

            for (Arista arista : actual.getConexiones()){
                Nodo vecino = arista.getDestino();

                if (cerrada.contains(vecino)) continue;

                double gTentativo = actual.getG() + arista.getCosto();

                if(!abierta.contains(vecino)) {
                    abierta.add(vecino);
                } else if (gTentativo >= vecino.getG()) {
                    continue;
                }

                vecino.setPadre(actual);
                vecino.setG(gTentativo);
                vecino.setH(calcularHeuristica(vecino, fin));
                vecino.setF(vecino.getG() + vecino.getH());

            }
        }
        return Collections.emptyList();
    }


    private Nodo obtenerNodoConMenorF (List<Nodo> abierta){
        Nodo mejor = abierta.get(0);
        for (Nodo nodo: abierta){
            if (nodo.getF() < mejor.getF()){
                mejor = nodo;
            }
        }
        return mejor;
    }

    private List<Nodo> reconstruirRuta (Nodo destino){
        List<Nodo> ruta = new ArrayList<>();
        Nodo actual = destino;
        while (actual != null){
            ruta.add(0, actual);
            actual = actual.getPadre();
        }
        return ruta;
    }

    private double calcularHeuristica(Nodo actual, Nodo objetivo){
        double dx = actual.getX() - objetivo.getX();
        double dy = actual.getY() - objetivo.getY();
        return Math.sqrt(dx *  dx + dy * dy);
    }

}
