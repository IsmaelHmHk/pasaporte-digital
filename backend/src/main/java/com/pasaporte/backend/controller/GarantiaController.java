package com.pasaporte.backend.controller;

import com.pasaporte.backend.entity.Garantia;
import com.pasaporte.backend.service.GarantiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/garantias")
@CrossOrigin(origins = "*")
public class GarantiaController {

    private final GarantiaService garantiaService;

    public GarantiaController(GarantiaService garantiaService) {
        this.garantiaService = garantiaService;
    }

    @GetMapping("/componente/{componenteId}")
    public ResponseEntity<Garantia> obtenerPorComponente(@PathVariable Long componenteId) {
        return garantiaService.obtenerPorComponenteId(componenteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> registrarGarantia(@RequestBody Garantia garantia) {
        try {
            Garantia nuevaGarantia = garantiaService.registrarGarantia(garantia);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaGarantia);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}