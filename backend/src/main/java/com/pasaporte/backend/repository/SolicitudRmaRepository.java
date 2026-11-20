package com.pasaporte.backend.repository;

import com.pasaporte.backend.entity.SolicitudRma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRmaRepository extends JpaRepository<SolicitudRma, Long> {
    List<SolicitudRma> findByUsuarioEmail(String email);
    Optional<SolicitudRma> findByCodigoRma(String codigoRma);
}