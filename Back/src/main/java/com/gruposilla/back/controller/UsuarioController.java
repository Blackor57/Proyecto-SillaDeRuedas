package com.gruposilla.back.controller;

import com.gruposilla.back.model.DTO.UsuarioRegistroDTO;
import com.gruposilla.back.model.entity.Usuario;
import com.gruposilla.back.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final UsuarioServicio usuarioServicio;

    public UsuarioController(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @PostMapping
    public ResponseEntity<String> registrarCuenta(@RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        usuarioServicio.save(usuarioRegistroDTO);
        return ResponseEntity.ok("Usuario registrado con éxito.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioServicio.pull(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.ok(usuarioServicio.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        usuarioServicio.delete(id);
        return ResponseEntity.ok("Usuario eliminado");
    }
}
