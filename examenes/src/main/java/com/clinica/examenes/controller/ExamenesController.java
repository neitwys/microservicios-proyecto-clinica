package com.clinica.examenes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clinica.examenes.dto.ExamenDetalleDTO;
import com.clinica.examenes.model.Examenes;
import com.clinica.examenes.service.ExamenesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/examenes")
public class ExamenesController {

    private final ExamenesService service;

    public ExamenesController(ExamenesService service) {
        this.service = service;
    }


    // POST - CREAR (201 CREATED)
    @PostMapping("/crear")
    public ResponseEntity<Examenes> guardarExamenes(
            @Valid @RequestBody Examenes examenes) {

        Examenes nueva = service.guardarExamenes(examenes);

        return ResponseEntity
                .status(201)
                .body(nueva);
    }


    // GET ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Examenes>> listar() {

        return ResponseEntity.ok(service.listar());
    }


    // GET DTO POR PACIENTE
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ExamenDetalleDTO>> obtenerPorPaciente(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.obtenerExamenDTO(id)
        );
    }


    // GET DTO POR ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ExamenDetalleDTO> buscarPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.obtenerExamenPorId(id)
        );
    }


    // PUT - ACTUALIZAR
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Examenes> actualizar(
            @PathVariable Integer id,
            @RequestBody Examenes examenActualizado) {

        return ResponseEntity.ok(
                service.actualizar(id, examenActualizado)
        );
    }


    // DELETE - ELIMINAR
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}