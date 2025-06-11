package com.gruposilla.back.repository;

import com.gruposilla.back.model.entity.MapaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface MapaRepository extends JpaRepository<MapaEntity, Long> {
    Optional<MapaEntity> findTopByOrderByIdDesc();
}
