package com.gruposilla.back.services;

import com.gruposilla.back.algorithm.graph.Nodo;

import java.util.List;

public interface AEstrellaIFA {

    List<Nodo> encontrarRuta(Nodo inicio, Nodo fin);
}
