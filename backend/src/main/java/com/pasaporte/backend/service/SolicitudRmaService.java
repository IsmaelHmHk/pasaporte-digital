package com.pasaporte.backend.service;

import com.pasaporte.backend.entity.HistorialEvento;
import com.pasaporte.backend.entity.SolicitudRma;
import com.pasaporte.backend.repository.HistorialEventoRepository;
import com.pasaporte.backend.repository.SolicitudRmaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolicitudRmaService {

    private final SolicitudRmaRepository solicitudRmaRepository;
    private final HistorialEventoRepository historialEventoRepository;

    public SolicitudRmaService(SolicitudRmaRepository solicitudRmaRepository,
                               HistorialEventoRepository historialEventoRepository) {
        this.solicitudRmaRepository = solicitudRmaRepository;
        this.historialEventoRepository = historialEventoRepository;
    }

    public List<SolicitudRma> obtenerTodas() {
        return solicitudRmaRepository.findAll();
    }

    public List<SolicitudRma> obtenerPorEstado(String estado) {
        return solicitudRmaRepository.findByEstado(estado);
    }

    @Transactional
    public SolicitudRma crearSolicitud(SolicitudRma solicitud) {
        // Regla de negocio: El código de RMA debe ser único
        if (solicitudRmaRepository.findByCodigoRma(solicitud.getCodigoRma()).isPresent()) {
            throw new IllegalArgumentException("El código de RMA " + solicitud.getCodigoRma() + " ya existe.");
        }

        SolicitudRma nuevaSolicitud = solicitudRmaRepository.save(solicitud);

        // Regla de negocio: Registrar automáticamente la apertura de RMA en la trazabilidad del componente
        HistorialEvento evento = new HistorialEvento(
                nuevaSolicitud.getComponente(),
                "RMA_ABIERTO",
                "Solicitud de RMA generada con código: " + nuevaSolicitud.getCodigoRma(),
                nuevaSolicitud.getUsuario()
        );
        historialEventoRepository.save(evento);

        return nuevaSolicitud;
    }
}