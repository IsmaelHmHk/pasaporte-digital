package com.pasaporte.backend.repository;

import com.pasaporte.backend.entity.HistorialEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEventoRepository extends JpaRepository<HistorialEvento, Long> {
    List<HistorialEvento> findByComponenteIdOrderByFechaEventoDesc(Long componenteId);
}