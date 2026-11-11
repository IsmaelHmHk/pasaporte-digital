package com.pasaporte.backend.repository;

import com.pasaporte.backend.entity.SolicitudRma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRmaRepository extends JpaRepository<SolicitudRma, Long> {
    Optional<SolicitudRma> findByCodigoRma(String codigoRma);
    List<SolicitudRma> findByEstado(String estado);
    List<SolicitudRma> findByUsuarioId(Long usuarioId);
}