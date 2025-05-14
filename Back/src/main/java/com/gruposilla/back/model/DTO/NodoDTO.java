package com.gruposilla.back.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodoDTO {
    private String identificador; // ej: "A", "B", "nodo1", etc.
    private double x;
    private double y;
}
