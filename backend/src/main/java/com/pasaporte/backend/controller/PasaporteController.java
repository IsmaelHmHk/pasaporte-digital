package com.pasaporte.backend.controller;

import com.pasaporte.backend.dto.PasaporteDTO;
import com.pasaporte.backend.service.ComponenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pasaporte")
@CrossOrigin(origins = "*")
public class PasaporteController {

    private final ComponenteService componenteService;

    public PasaporteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping("/{numSerie}")
    public ResponseEntity<PasaporteDTO> verificarPasaporte(@PathVariable String numSerie) {
        return componenteService.obtenerPorNumSerie(numSerie)
                .map(componente -> ResponseEntity.ok(new PasaporteDTO(componente)))
                .orElse(ResponseEntity.notFound().build());
    }
}