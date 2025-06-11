package com.gruposilla.back.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private int x;
    private int y;
    private int ancho;
    private int alto;
    private String tipo;
    private List<MuebleDTO> muebles = new ArrayList<>();
}
