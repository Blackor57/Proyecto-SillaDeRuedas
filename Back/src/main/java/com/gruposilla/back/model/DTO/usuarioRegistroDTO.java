package com.gruposilla.back.model.DTO;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class usuarioRegistroDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String password;

}
