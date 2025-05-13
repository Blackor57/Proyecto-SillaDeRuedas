package com.gruposilla.back.repository;

import com.gruposilla.back.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRegistro extends JpaRepository<Usuario, Long> {
}
