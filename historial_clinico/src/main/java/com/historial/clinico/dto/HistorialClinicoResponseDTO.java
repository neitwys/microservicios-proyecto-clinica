package com.historial.clinico.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialClinicoResponseDTO {

    private Long id;

    private Long idPaciente;

    private String nombrePaciente;

    private LocalDate fechaConsulta;

    private String diagnostico;

    private String medicoResponsable;
}
