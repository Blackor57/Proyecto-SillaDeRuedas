package com.gruposilla.back.services.IMPL;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.model.entity.Usuario;
import com.gruposilla.back.repository.UsuarioRepository;
import com.gruposilla.back.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioRegistroIMPL implements UsuarioServicio {

    private final UsuarioRepository usuarioRepository;

    public UsuarioRegistroIMPL(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


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
    public Usuario update(long id, UsuarioRegistroDTO registroDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // Modificamos los datos existentes
        usuario.setNombre(registroDTO.getNombre());
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setPassword(registroDTO.getPassword());

        // Guardamos los cambios
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario pull(long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
