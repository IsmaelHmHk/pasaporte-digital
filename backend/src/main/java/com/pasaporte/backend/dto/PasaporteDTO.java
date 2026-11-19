package com.pasaporte.backend.dto;

import com.pasaporte.backend.entity.Componente;
import java.time.LocalDateTime;

public class PasaporteDTO {

    private String numSerie;
    private String modelo;
    private String fabricante;
    private String tipoComponente;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaFinGarantia;
    private boolean enGarantia;

    public PasaporteDTO(Componente componente) {
        this.numSerie = componente.getNumSerie();
        this.modelo = componente.getModelo();
        this.fabricante = componente.getFabricante();
        this.tipoComponente = componente.getTipoComponente();
        this.estado = componente.getEstado();
        this.fechaRegistro = componente.getFechaRegistro();

        if (this.fechaRegistro != null) {
            this.fechaFinGarantia = this.fechaRegistro.plusYears(3);
            this.enGarantia = LocalDateTime.now().isBefore(this.fechaFinGarantia);
        } else {
            this.enGarantia = false;
        }
    }

    public String getNumSerie() { return numSerie; }
    public String getModelo() { return modelo; }
    public String getFabricante() { return fabricante; }
    public String getTipoComponente() { return tipoComponente; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public LocalDateTime getFechaFinGarantia() { return fechaFinGarantia; }
    public boolean isEnGarantia() { return enGarantia; }
}