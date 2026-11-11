package com.pasaporte.backend.repository;

import com.pasaporte.backend.entity.Garantia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GarantiaRepository extends JpaRepository<Garantia, Long> {
    Optional<Garantia> findByComponenteId(Long componenteId);
}