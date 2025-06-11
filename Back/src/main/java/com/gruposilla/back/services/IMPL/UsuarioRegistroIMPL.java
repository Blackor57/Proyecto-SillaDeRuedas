package com.gruposilla.back.services.IMPL;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.model.entity.Usuario;
import com.gruposilla.back.repository.UsuarioRepository;
import com.gruposilla.back.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UsuarioRegistroIMPL implements UsuarioServicio, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioRegistroIMPL(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Usuario save(UsuarioRegistroDTO registroDTO) {
        Usuario usuario = Usuario.builder()
                .correo(registroDTO.getCorreo())
                .password(passwordEncoder.encode(registroDTO.getPassword()))
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

        usuario.setNombre(registroDTO.getNombre());
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities("USER") // Si usas roles, cámbialo por usuario.getRol() o similar
                .build();
    }
}
