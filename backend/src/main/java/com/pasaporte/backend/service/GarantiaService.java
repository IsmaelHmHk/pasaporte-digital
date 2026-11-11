package com.pasaporte.backend.service;

import com.pasaporte.backend.entity.Garantia;
import com.pasaporte.backend.repository.GarantiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GarantiaService {

    private final GarantiaRepository garantiaRepository;

    public GarantiaService(GarantiaRepository garantiaRepository) {
        this.garantiaRepository = garantiaRepository;
    }

    public Optional<Garantia> obtenerPorComponenteId(Long componenteId) {
        return garantiaRepository.findByComponenteId(componenteId);
    }

    @Transactional
    public Garantia registrarGarantia(Garantia garantia) {
        // Regla de negocio: Un componente solo puede tener una garantía vinculada
        if (garantiaRepository.findByComponenteId(garantia.getComponente().getId()).isPresent()) {
            throw new IllegalArgumentException("El componente ya tiene una garantía registrada.");
        }
        return garantiaRepository.save(garantia);
    }
}