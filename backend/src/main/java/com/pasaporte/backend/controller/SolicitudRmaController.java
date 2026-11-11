package com.pasaporte.backend.controller;

import com.pasaporte.backend.entity.SolicitudRma;
import com.pasaporte.backend.service.SolicitudRmaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-rma")
@CrossOrigin(origins = "*")
public class SolicitudRmaController {

    private final SolicitudRmaService solicitudRmaService;

    public SolicitudRmaController(SolicitudRmaService solicitudRmaService) {
        this.solicitudRmaService = solicitudRmaService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudRma>> listarTodas(
            @RequestParam(required = false) String estado) {
        if (estado != null && !estado.isEmpty()) {
            return ResponseEntity.ok(solicitudRmaService.obtenerPorEstado(estado));
        }
        return ResponseEntity.ok(solicitudRmaService.obtenerTodas());
    }

    @PostMapping
    public ResponseEntity<?> crearSolicitud(@RequestBody SolicitudRma solicitud) {
            SolicitudRma nuevaSolicitud = solicitudRmaService.crearSolicitud(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);

    }
}