package com.historial.clinico.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecetaEnHistorialDTO {
    private Integer idReceta;
    private Integer idCita;
    private Integer idPaciente;
    private String medicamento;
    private String indicaciones;
    private LocalDate fechaEmision;
}
