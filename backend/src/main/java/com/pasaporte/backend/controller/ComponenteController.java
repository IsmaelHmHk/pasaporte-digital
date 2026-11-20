package com.pasaporte.backend.controller;

import com.pasaporte.backend.entity.Componente;
import com.pasaporte.backend.service.ComponenteService;
import com.pasaporte.backend.service.PdfService;
import com.pasaporte.backend.service.QrService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/componentes")
@CrossOrigin(origins = "*")
public class ComponenteController {

    private final ComponenteService componenteService;
    private final QrService qrService;
    private final PdfService pdfService;

    public ComponenteController(ComponenteService componenteService, QrService qrService, PdfService pdfService) {
        this.componenteService = componenteService;
        this.qrService = qrService;
        this.pdfService = pdfService;
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

    @GetMapping(value = "/{numSerie}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generarQrComponente(@PathVariable String numSerie) {
        return componenteService.obtenerPorNumSerie(numSerie)
                .map(componente -> {
                    String urlPasaporte = "http://localhost:8080/api/pasaporte/" + componente.getNumSerie();
                    byte[] imagenQr = qrService.generarCodigoQr(urlPasaporte, 300, 300);
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(imagenQr);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{numSerie}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generarPdfComponente(@PathVariable String numSerie) {
        return componenteService.obtenerPorNumSerie(numSerie)
                .map(componente -> {
                    byte[] pdfBytes = pdfService.generarPasaportePdf(componente);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("inline", "pasaporte-" + componente.getNumSerie() + ".pdf");
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(pdfBytes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @PostMapping
    public ResponseEntity<Componente> registrarComponente(@RequestBody Componente componente) {
        Componente nuevoComponente = componenteService.registrarComponente(componente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoComponente);
    }
}