package com.gruposilla.back.services;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.model.entity.Usuario;

public interface UsuarioServicio {

    public Usuario save(UsuarioRegistroDTO registroDTO);

    public Usuario update();

    public Usuario delete();

    public Usuario pull();


}
