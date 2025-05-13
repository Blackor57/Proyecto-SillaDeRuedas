package com.gruposilla.back.services.IMPL;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.model.entity.Usuario;
import com.gruposilla.back.repository.UsuarioRepository;
import com.gruposilla.back.services.UsuarioServicio;

public class UsuarioRegistroIMPL implements UsuarioServicio {


    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario save(UsuarioRegistroDTO registroDTO) {
        Usuario usuario = Usuario.builder()
                .correo(registroDTO.getCorreo())
                .password(registroDTO.getPassword())
                .build();
        usuario = usuarioRepository.save(usuario);

        String nombreGenerado = "usuario" + usuario.getId();
        usuario.setNombre(nombreGenerado);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update() {
        return null;
    }

    @Override
    public Usuario delete() {
        return null;
    }

    @Override
    public Usuario pull() {
        return null;
    }

}
