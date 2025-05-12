package com.gruposilla.back.services;

import com.gruposilla.back.algorithm.graph.Nodo;

import java.util.List;

public interface aEstrellaIFA {

    List<Nodo> encontrarRuta(Nodo inicio, Nodo fin);
}
