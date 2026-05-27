package com.clinica.soporte.model;

public class Cita {

    private Long id;
    private String paciente;
    private String fecha;
    private String estado;
    private String observacion;

    public Cita() {
    }

    public Cita(Long id, String paciente, String fecha, String estado, String observacion) {
        this.id = id;
        this.paciente = paciente;
        this.fecha = fecha;
        this.estado = estado;
        this.observacion = observacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
