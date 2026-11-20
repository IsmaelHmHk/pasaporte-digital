package com.pasaporte.backend.dto;

public class CrearRmaRequest {
    private String numSerie;
    private String motivo;

    public String getNumSerie() { return numSerie; }
    public void setNumSerie(String numSerie) { this.numSerie = numSerie; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}