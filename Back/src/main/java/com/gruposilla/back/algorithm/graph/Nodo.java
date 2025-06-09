package com.gruposilla.back.algorithm.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private double x;
    private double y;

    private List<Arista> conexiones;

    private double g;
    private double h;
    private double f;



    @JsonIgnore
    private Nodo padre;
}
