package com.gruposilla.back.model.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CoordenadasRequest {
    private double inicioX;
    private double inicioY;

    private double finX;
    private double finY;

}
