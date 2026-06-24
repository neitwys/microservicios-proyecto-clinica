package com.clinica.pagos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinica.pagos.dto.PagoCrearDTO;
import com.clinica.pagos.dto.PagoMostrarDTO;
import com.clinica.pagos.service.PagoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @Operation(
        summary = "Listar pagos",
        description = "Obtiene todos los pagos registrados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<PagoMostrarDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(
        summary = "Buscar pago por ID",
        description = "Obtiene un pago específico mediante su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<PagoMostrarDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(
        summary = "Crear pago",
        description = "Registra un nuevo pago en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<PagoMostrarDTO> crearPago(@Valid @RequestBody PagoCrearDTO dto) {
        return ResponseEntity.ok(service.registrarPago(dto));
    }

    @Operation(
        summary = "Eliminar pago",
        description = "Elimina un pago del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPago(@PathVariable Integer id) {
        service.eliminarPorId(id);
        return ResponseEntity.ok("Pago eliminado exitosamente con id " + id);
    }

    @Operation(
        summary = "Actualizar pago",
        description = "Actualiza los datos de un pago existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PagoMostrarDTO> actualizarPago(@PathVariable Integer id, @Valid @RequestBody PagoCrearDTO dto) {
        return ResponseEntity.ok(service.actualizarPago(id, dto));
    }

    
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> manejarPagoDuplicado(IllegalStateException ex) {
    return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(ex.getMessage());
}
}