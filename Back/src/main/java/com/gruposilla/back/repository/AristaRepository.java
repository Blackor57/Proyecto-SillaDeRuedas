package com.gruposilla.back.repository;

import com.gruposilla.back.model.entity.AristaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AristaRepository extends JpaRepository<AristaEntity, Long> {
    @Query("SELECT a FROM AristaEntity a WHERE a.origen.usuario.id = :idUsuario")
    List<AristaEntity> findByUsuario(@Param("idUsuario") Long idUsuario);
}
