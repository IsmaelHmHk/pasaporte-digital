package com.pasaporte.backend.dto;

public class ActualizarEstadoRmaRequest {
    private String estado;
    private String respuestaTecnico;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRespuestaTecnico() { return respuestaTecnico; }
    public void setRespuestaTecnico(String respuestaTecnico) { this.respuestaTecnico = respuestaTecnico; }
}