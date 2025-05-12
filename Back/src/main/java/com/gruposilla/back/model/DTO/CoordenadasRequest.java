package com.gruposilla.back.model.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CoordenadasRequest {
    private String inicio;
    private String fin;
}
