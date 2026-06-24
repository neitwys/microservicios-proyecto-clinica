package com.historial.clinico.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaEnHistorialDTO {
    private Integer idCita;
    private Integer idPaciente;
    private Integer idMedico;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private String motivo;
    private Integer idPago;
}
