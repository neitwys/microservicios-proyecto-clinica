package com.historial.clinico.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoEnHistorialDTO {
    private Integer id;
    private Double monto;
    private String estado;
    private String metodoPago;
    private LocalDateTime fechaCreacion;
    private Integer idCita;
    private Integer idPaciente;
    private String nombrePaciente;
}
