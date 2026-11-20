package com.pasaporte.backend.dto;

import com.pasaporte.backend.entity.SolicitudRma;
import java.time.LocalDateTime;

public class SolicitudRmaDTO {
    private Long id;
    private String codigoRma;
    private String numSerieComponente;
    private String modeloComponente;
    private String emailUsuario;
    private String motivo;
    private String estado;
    private String respuestaTecnico;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaActualizacion;

    public SolicitudRmaDTO(SolicitudRma rma) {
        this.id = rma.getId();
        this.codigoRma = rma.getCodigoRma();
        this.numSerieComponente = rma.getComponente().getNumSerie();
        this.modeloComponente = rma.getComponente().getModelo();
        this.emailUsuario = rma.getUsuario().getEmail();
        this.motivo = rma.getMotivo();
        this.estado = rma.getEstado();
        this.respuestaTecnico = rma.getRespuestaTecnico();
        this.fechaSolicitud = rma.getFechaSolicitud();
        this.fechaActualizacion = rma.getFechaActualizacion();
    }

    public Long getId() { return id; }
    public String getCodigoRma() { return codigoRma; }
    public String getNumSerieComponente() { return numSerieComponente; }
    public String getModeloComponente() { return modeloComponente; }
    public String getEmailUsuario() { return emailUsuario; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }
    public String getRespuestaTecnico() { return respuestaTecnico; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
}