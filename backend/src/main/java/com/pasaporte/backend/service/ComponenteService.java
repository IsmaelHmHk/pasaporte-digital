package com.pasaporte.backend.service;

import com.pasaporte.backend.entity.Componente;
import com.pasaporte.backend.entity.HistorialEvento;
import com.pasaporte.backend.repository.ComponenteRepository;
import com.pasaporte.backend.repository.HistorialEventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComponenteService {

    private final ComponenteRepository componenteRepository;
    private final HistorialEventoRepository historialEventoRepository;

    // Inyección de dependencias por constructor
    public ComponenteService(ComponenteRepository componenteRepository,
                             HistorialEventoRepository historialEventoRepository) {
        this.componenteRepository = componenteRepository;
        this.historialEventoRepository = historialEventoRepository;
    }

    public List<Componente> obtenerTodos() {
        return componenteRepository.findAll();
    }

    public Optional<Componente> obtenerPorNumSerie(String numSerie) {
        return componenteRepository.findByNumSerie(numSerie);
    }

    @Transactional
    public Componente registrarComponente(Componente componente) {
        // Regla de negocio: Verificar si el número de serie ya existe
        if (componenteRepository.findByNumSerie(componente.getNumSerie()).isPresent()) {
            throw new IllegalArgumentException("El número de serie " + componente.getNumSerie() + " ya está registrado.");
        }

        Componente guardado = componenteRepository.save(componente);

        // Regla de negocio: Generar evento de trazabilidad automático
        HistorialEvento evento = new HistorialEvento(
                guardado,
                "REGISTRO",
                "Componente dado de alta en el sistema del Pasaporte Digital.",
                guardado.getUsuario()
        );
        historialEventoRepository.save(evento);

        return guardado;
    }
}