package com.clinica.examenes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinica.examenes.dto.ExamenDetalleDTO;
import com.clinica.examenes.model.Examenes;
import com.clinica.examenes.service.ExamenesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/examenes")
public class ExamenesController {

    private final ExamenesService service;

    public ExamenesController(ExamenesService service) {
        this.service = service;
    }

    @Operation(
        summary = "Crear examen",
        description = "Registra un nuevo examen en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Examen creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<Examenes> guardarExamenes(
            @Valid @RequestBody Examenes examenes) {

        Examenes nueva = service.guardarExamenes(examenes);

        return ResponseEntity
                .status(201)
                .body(nueva);
    }


    @Operation(
        summary = "Listar exámenes",
        description = "Obtiene una lista con todos los exámenes registrados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<Examenes>> listar() {

        return ResponseEntity.ok(service.listar());
    }


    @Operation(
        summary = "Buscar exámenes por paciente",
        description = "Obtiene todos los exámenes asociados a un paciente mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Exámenes encontrados"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ExamenDetalleDTO>> obtenerPorPaciente(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.obtenerExamenDTO(id)
        );
    }


    @Operation(
        summary = "Buscar examen por ID",
        description = "Obtiene la información detallada de un examen mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Examen encontrado"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ExamenDetalleDTO> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.obtenerExamenPorId(id)
        );
    }


    @Operation(
        summary = "Actualizar examen",
        description = "Actualiza la información de un examen existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Examen actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Examenes> actualizar(
            @PathVariable Integer id,
            @RequestBody Examenes examenActualizado) {

        return ResponseEntity.ok(
                service.actualizar(id, examenActualizado)
        );
    }


    @Operation(
        summary = "Eliminar examen",
        description = "Elimina un examen registrado mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Examen eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}