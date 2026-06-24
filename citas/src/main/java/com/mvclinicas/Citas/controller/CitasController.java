package com.mvclinicas.Citas.controller;

import com.mvclinicas.Citas.model.Citas;
import com.mvclinicas.Citas.service.CitasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitasController {

    @Autowired
    private CitasService service;

   
    @Operation(
        summary = "Crear cita",
        description = "Registra una nueva cita médica en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cita creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<Citas> crear(@Valid @RequestBody Citas cita) {

        Citas nuevaCita = service.crearCita(cita);

        return ResponseEntity.status(201).body(nuevaCita);
    }

    
    @Operation(
        summary = "Buscar cita por ID",
        description = "Obtiene una cita específica mediante su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Citas> obtener(@PathVariable Integer id) {

        return ResponseEntity.ok(service.obtenerPorId(id));
    }


    @Operation(
        summary = "Listar citas",
        description = "Obtiene todas las citas registradas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Citas>> listar() {

        return ResponseEntity.ok(service.listar());
    }

   
    @Operation(
        summary = "Listar citas por paciente",
        description = "Obtiene todas las citas asociadas a un paciente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas encontradas"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Citas>> listarPorPaciente(@PathVariable Integer idPaciente) {

        return ResponseEntity.ok(service.listarPorPaciente(idPaciente));
    }

  
    @Operation(
        summary = "Listar citas por médico",
        description = "Obtiene todas las citas asociadas a un médico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas encontradas"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Citas>> listarPorMedico(@PathVariable Integer idMedico) {

        return ResponseEntity.ok(service.listarPorMedico(idMedico));
    }

    @Operation(
        summary = "Actualizar cita",
        description = "Actualiza los datos de una cita existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Citas> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Citas cita) {

        return ResponseEntity.ok(service.actualizar(id, cita));
    }

    @Operation(
        summary = "Confirmar cita",
        description = "Marca una cita como confirmada"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita confirmada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Citas> confirmar(@PathVariable Integer id, @RequestParam Integer idPago) {
        return ResponseEntity.ok(service.confirmarCita(id, idPago));
    }

    @Operation(
        summary = "Cancelar cita",
        description = "Cancela una cita existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita cancelada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Citas> cancelar(@PathVariable Integer id) {

        return ResponseEntity.ok(service.cancelarCita(id));
    }

    @Operation(
        summary = "Eliminar cita",
        description = "Elimina una cita del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cita eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> manejarCitaYaConfirmada(IllegalStateException ex) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}