package com.gruposilla.back.model.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapaRequest {
    private List<NodoDTO> nodos;
    private List<AristaDTO> aristas;
}
