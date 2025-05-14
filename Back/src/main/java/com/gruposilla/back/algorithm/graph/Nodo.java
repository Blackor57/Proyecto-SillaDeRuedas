package com.gruposilla.back.algorithm.graph;

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

public class Nodo {

    private long id;

    private double x, y;

    private List<Arista> conexiones;

    private double g;
    private double h;
    private double f;

    private Nodo padre;
}
