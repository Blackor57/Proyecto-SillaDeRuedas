package com.gruposilla.back.model.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UsuarioRegistroDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String password;

}
