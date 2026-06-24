package com.clinica.autenticacion.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String rut;

    @NotBlank
    private String nombre;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    private String clave;

    @NotBlank
    private String rol; //"PACIENTE", "MEDICO", "ADMIN"

    private String especialidad;

    private String telefono;

    @NotNull
    private LocalDate fechaNacimiento;

    private Boolean estado;
}
