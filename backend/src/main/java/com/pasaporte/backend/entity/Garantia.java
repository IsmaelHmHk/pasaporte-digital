package com.pasaporte.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "garantias")
public class Garantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "componente_id", nullable = false, unique = true)
    private Componente componente;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "tipo_garantia", nullable = false, length = 50)
    private String tipoGarantia;

    @Column(nullable = false, length = 50)
    private String estado;

    // Constructor vacío exigido por JPA
    public Garantia() {}

    // Constructor con parámetros
    public Garantia(Componente componente, LocalDate fechaInicio, LocalDate fechaFin, String tipoGarantia, String estado) {
        this.componente = componente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoGarantia = tipoGarantia;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Componente getComponente() { return componente; }
    public void setComponente(Componente componente) { this.componente = componente; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getTipoGarantia() { return tipoGarantia; }
    public void setTipoGarantia(String tipoGarantia) { this.tipoGarantia = tipoGarantia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}