package com.pasaporte.backend.service;

import com.pasaporte.backend.dto.CrearRmaRequest;
import com.pasaporte.backend.dto.SolicitudRmaDTO;
import com.pasaporte.backend.entity.Componente;
import com.pasaporte.backend.entity.SolicitudRma;
import com.pasaporte.backend.entity.Usuario;
import com.pasaporte.backend.repository.ComponenteRepository;
import com.pasaporte.backend.repository.SolicitudRmaRepository;
import com.pasaporte.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitudRmaService {

    private final SolicitudRmaRepository rmaRepository;
    private final ComponenteRepository componenteRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitudRmaService(SolicitudRmaRepository rmaRepository,
                               ComponenteRepository componenteRepository,
                               UsuarioRepository usuarioRepository) {
        this.rmaRepository = rmaRepository;
        this.componenteRepository = componenteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SolicitudRmaDTO crearSolicitud(CrearRmaRequest request, String emailUsuario) {
        if (request.getNumSerie() == null || request.getNumSerie().isBlank()) {
            throw new IllegalArgumentException("El número de serie es obligatorio");
        }
        if (request.getMotivo() == null || request.getMotivo().isBlank()) {
            throw new IllegalArgumentException("Debes indicar el motivo de la avería");
        }

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Componente componente = componenteRepository.findByNumSerie(request.getNumSerie())
                .orElseThrow(() -> new IllegalArgumentException("No existe ningún componente con ese número de serie"));

        if ("EN_RMA".equalsIgnoreCase(componente.getEstado())) {
            throw new IllegalArgumentException("Este componente ya tiene una solicitud de RMA abierta");
        }

        if (componente.getFechaRegistro() == null ||
                LocalDateTime.now().isAfter(componente.getFechaRegistro().plusYears(3))) {
            throw new IllegalArgumentException("Garantía expirada: han pasado más de 3 años desde el registro del componente");
        }

        String codigoGenerado = "RMA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        componente.setEstado("EN_RMA");
        componenteRepository.save(componente);

        SolicitudRma nuevaSolicitud = new SolicitudRma(
                componente,
                usuario,
                codigoGenerado,
                request.getMotivo(),
                "SOLICITADO"
        );

        SolicitudRma guardada = rmaRepository.save(nuevaSolicitud);
        return new SolicitudRmaDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<SolicitudRmaDTO> listarSolicitudes(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String rol = String.valueOf(usuario.getRol());
        List<SolicitudRma> lista;

        if ("ADMIN".equals(rol) || "TECNICO".equals(rol)) {
            lista = rmaRepository.findAll();
        } else {
            lista = rmaRepository.findByUsuarioEmail(emailUsuario);
        }

        return lista.stream().map(SolicitudRmaDTO::new).toList();
    }

    @Transactional
    public SolicitudRmaDTO actualizarEstado(Long idRma, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }

        String estadoNormalizado = nuevoEstado.trim().toUpperCase();
        List<String> estadosValidos = List.of("EN_REVISION", "REPARADO", "RECHAZADO");

        if (!estadosValidos.contains(estadoNormalizado)) {
            throw new IllegalArgumentException("Estado no válido. Valores permitidos: EN_REVISION, REPARADO, RECHAZADO");
        }

        SolicitudRma rma = rmaRepository.findById(idRma)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud RMA no encontrada"));

        rma.setEstado(estadoNormalizado);

        Componente componente = rma.getComponente();
        if ("REPARADO".equals(estadoNormalizado) || "RECHAZADO".equals(estadoNormalizado)) {
            componente.setEstado("OPERATIVO");
            componenteRepository.save(componente);
        }

        SolicitudRma actualizada = rmaRepository.save(rma);
        return new SolicitudRmaDTO(actualizada);
    }
}