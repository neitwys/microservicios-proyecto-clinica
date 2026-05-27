package com.historial.clinico.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClinicoDTO {

    @NotNull(message = "El idPaciente es obligatorio")
    private Long idPaciente;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nombrePaciente;

    @NotNull(message = "La fecha de consulta es obligatoria")
    private LocalDate fechaConsulta;

    @NotBlank(message = "El diagnostico es obligatorio")
    private String diagnostico;

    @NotBlank(message = "El tratamiento es obligatorio")
    private String tratamiento;

    private String observaciones;

    @NotBlank(message = "El medico responsable es obligatorio")
    private String medicoResponsable;
}
