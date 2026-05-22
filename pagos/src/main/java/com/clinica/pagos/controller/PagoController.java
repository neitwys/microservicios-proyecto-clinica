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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pagos")
public class PagoController {
    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PagoMostrarDTO>> obtenerTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<PagoMostrarDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/crear")
    public ResponseEntity<PagoMostrarDTO> crearPago(@Valid @RequestBody PagoCrearDTO dto) {
        return ResponseEntity.ok(service.registrarPago(dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPago(@PathVariable Integer id) {
        service.eliminarPorId(id);
        return ResponseEntity.ok("Pago eliminado exitosamente con id " + id);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PagoMostrarDTO> actualizarPago(@PathVariable Integer id, @Valid @RequestBody PagoCrearDTO dto) {
        return ResponseEntity.ok(service.actualizarPago(id, dto));
    }
}