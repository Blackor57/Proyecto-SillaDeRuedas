package com.gruposilla.back.repository;

import com.gruposilla.back.model.entity.NodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodoRepository extends JpaRepository<NodoEntity, Long> {
    List<NodoEntity> findByUsuario_Id(Long idUsuario);

    @Query("SELECT n FROM NodoEntity n WHERE ABS(n.x - :x) < 0.001 AND ABS(n.y - :y) < 0.001")
    NodoEntity findByXAndY(@Param("x") double x, @Param("y") double y);
}
