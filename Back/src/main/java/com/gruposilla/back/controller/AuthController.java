package com.gruposilla.back.controller;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.repository.UsuarioRepository;
import com.gruposilla.back.security.JwtUtil;
import org.springframework.security.core.userdetails.User;
import com.gruposilla.back.services.UsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioServicio usuarioServicio;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioServicio usuarioServicio, UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioServicio = usuarioServicio;
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UsuarioRegistroDTO dto) {
        if (usuarioRepository.findByCorreo(dto.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body("Correo ya en uso");
        }

        usuarioServicio.save(dto);
        return ResponseEntity.ok("Usuario registrado");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioRegistroDTO dto) {
        var usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new UsernameNotFoundException("Correo o contraseña inválidos"));

        if (!new BCryptPasswordEncoder().matches(dto.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        UserDetails userDetails = User
                .withUsername(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(new ArrayList<>())
                .build();
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
