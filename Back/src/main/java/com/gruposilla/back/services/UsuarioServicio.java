package com.gruposilla.back.services;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.model.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioServicio {

    public Usuario save(UsuarioRegistroDTO registroDTO);

    public Usuario update(long id, UsuarioRegistroDTO registroDTO);

    public void delete(long id);

    public Usuario pull(long id);

}
