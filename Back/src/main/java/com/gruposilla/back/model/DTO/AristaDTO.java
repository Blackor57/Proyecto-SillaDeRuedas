package com.gruposilla.back.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AristaDTO {
    private String origenId;
    private String destinoId;
    private double costo;
}
