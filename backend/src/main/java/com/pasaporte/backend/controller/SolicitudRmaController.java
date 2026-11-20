package com.pasaporte.backend.controller;

import com.pasaporte.backend.dto.ActualizarEstadoRmaRequest;
import com.pasaporte.backend.dto.CrearRmaRequest;
import com.pasaporte.backend.dto.SolicitudRmaDTO;
import com.pasaporte.backend.service.SolicitudRmaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rmas")
@CrossOrigin(origins = "*")
public class SolicitudRmaController {

    private final SolicitudRmaService rmaService;

    public SolicitudRmaController(SolicitudRmaService rmaService) {
        this.rmaService = rmaService;
    }

    @PostMapping
    public ResponseEntity<SolicitudRmaDTO> abrirRma(@RequestBody CrearRmaRequest request,
                                                    Authentication authentication) {
        SolicitudRmaDTO creada = rmaService.crearSolicitud(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<SolicitudRmaDTO>> listarRmas(Authentication authentication) {
        return ResponseEntity.ok(rmaService.listarSolicitudes(authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudRmaDTO> cambiarEstadoRma(@PathVariable Long id,
                                                            @RequestBody ActualizarEstadoRmaRequest request) {
        return ResponseEntity.ok(rmaService.actualizarEstado(id, request.getEstado(), request.getRespuestaTecnico()));
    }
}