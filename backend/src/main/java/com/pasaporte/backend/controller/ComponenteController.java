package com.pasaporte.backend.controller;

import com.pasaporte.backend.entity.Componente;
import com.pasaporte.backend.service.ComponenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/componentes")
@CrossOrigin(origins = "*")
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping
    public ResponseEntity<List<Componente>> listarTodos() {
        return ResponseEntity.ok(componenteService.obtenerTodos());
    }

    @GetMapping("/{numSerie}")
    public ResponseEntity<Componente> obtenerPorNumSerie(@PathVariable String numSerie) {
        return componenteService.obtenerPorNumSerie(numSerie)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> registrarComponente(@RequestBody Componente componente) {
        try {
            Componente nuevoComponente = componenteService.registrarComponente(componente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComponente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}