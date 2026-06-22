package com.clinica.recetas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.clinica.recetas.DTO.RecetaCrearDTO;
import com.clinica.recetas.DTO.RecetaDetalleDTO;
import com.clinica.recetas.DTO.RecetaMostrarDTO;
import com.clinica.recetas.service.RecetasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recetas")
public class RecetaController {

    private final RecetasService service;

    public RecetaController(RecetasService service) {
        this.service = service;
    }

    @Operation(
        summary = "Listar recetas por paciente (detalle)",
        description = "Obtiene el detalle completo de recetas asociadas a un paciente mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recetas obtenidas correctamente"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/detalle/paciente/{idPaciente}")
    public List<RecetaDetalleDTO> detallePaciente(@PathVariable Integer idPaciente) {
        return service.listarDetallePaciente(idPaciente);
    }


    @Operation(
        summary = "Crear receta",
        description = "Registra una nueva receta en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Receta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public RecetaMostrarDTO crear(
            @Valid @RequestBody RecetaCrearDTO dto) {

        return service.crearReceta(dto);
    }


    @Operation(
        summary = "Listar recetas",
        description = "Obtiene todas las recetas registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public List<RecetaMostrarDTO> listar() {
        return service.listarTodas();
    }


    @Operation(
        summary = "Buscar receta por ID",
        description = "Obtiene una receta específica mediante su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta encontrada"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public RecetaMostrarDTO buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }


    @Operation(
        summary = "Listar recetas por paciente",
        description = "Obtiene todas las recetas asociadas a un paciente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recetas encontradas"),
        @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/paciente/{idPaciente}")
    public List<RecetaMostrarDTO> listarPorPaciente(@PathVariable Integer idPaciente) {
        return service.listarPorPaciente(idPaciente);
    }


    @Operation(
        summary = "Listar recetas por médico",
        description = "Obtiene todas las recetas asociadas a un médico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recetas encontradas"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/medico/{idMedico}")
    public List<RecetaMostrarDTO> listarPorMedico(@PathVariable Integer idMedico) {
        return service.listarPorMedico(idMedico);
    }


    @Operation(
        summary = "Eliminar receta",
        description = "Elimina una receta por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        RecetaMostrarDTO receta = service.buscarPorId(id);

        if (receta != null) {
            service.eliminarPorId(id);
            return "Receta eliminada";
        }

        return "Receta no encontrada";
    }


    @Operation(
        summary = "Actualizar receta",
        description = "Actualiza una receta existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaCrearDTO dto) {

        RecetaMostrarDTO receta = service.actualizarReceta(id, dto);

        if (receta != null) {
            return "Receta actualizada";
        }

        return "Receta no encontrada";
    }
}