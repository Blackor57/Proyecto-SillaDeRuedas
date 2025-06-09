package com.gruposilla.back.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class NodoDTO {
    private String identificador; // ej: "A", "B", "nodo1", etc.
    private double x;
    private double y;
}
