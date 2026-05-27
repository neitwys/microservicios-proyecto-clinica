package com.historial.clinico.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "historial_clinico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del paciente es obligatorio")
    @Column(nullable = false)
    private Long idPaciente;

    @NotBlank(message = "El nombre del paciente no puede estar vacío")
    @Column(nullable = false, length = 100)
    private String nombrePaciente;

    @NotNull(message = "La fecha de consulta es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaConsulta;

    @NotBlank(message = "El diagnóstico no puede estar vacío")
    @Column(nullable = false, length = 200)
    private String diagnostico;

    @NotBlank(message = "El tratamiento no puede estar vacío")
    @Column(nullable = false, length = 300)
    private String tratamiento;

    @Column(length = 500)
    private String observaciones;

    @NotBlank(message = "El médico responsable no puede estar vacío")
    @Column(nullable = false, length = 80)
    private String medicoResponsable;
}
