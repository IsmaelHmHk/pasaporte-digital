package com.pasaporte.backend.repository;

import com.pasaporte.backend.entity.Componente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComponenteRepository extends JpaRepository<Componente, Long> {

    Optional<Componente> findByNumSerie(String numSerie);

    List<Componente> findByUsuarioId(Long usuarioId);

    List<Componente> findByEstado(String estado);
}