package com.mvclinicas.Citas.controller;

import com.mvclinicas.Citas.model.Citas;
import com.mvclinicas.Citas.service.CitasService;

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


    // POST
    @PostMapping("/crear")
    public ResponseEntity<Citas> crear(
            @Valid @RequestBody Citas cita) {

        Citas nuevaCita = service.crearCita(cita);

        return ResponseEntity
                .status(201)
                .body(nuevaCita);
    }


    // GET BY ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Citas> obtener(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.obtenerPorId(id)
        );
    }


    // GET ALL
    @GetMapping("/listar")
    public ResponseEntity<List<Citas>> listar() {

        return ResponseEntity.ok(
                service.listar()
        );
    }


    // GET BY PACIENTE
    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<Citas>> listarPorPaciente(
            @PathVariable Integer idPaciente) {

        return ResponseEntity.ok(
                service.listarPorPaciente(idPaciente)
        );
    }


    // GET BY MEDICO
    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<Citas>> listarPorMedico(
            @PathVariable Integer idMedico) {

        return ResponseEntity.ok(
                service.listarPorMedico(idMedico)
        );
    }


    // PUT ACTUALIZAR
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Citas> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Citas cita) {

        return ResponseEntity.ok(
                service.actualizar(id, cita)
        );
    }


    // PUT CONFIRMAR
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Citas> confirmar(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.confirmarCita(id)
        );
    }


    // PUT CANCELAR
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Citas> cancelar(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                service.cancelarCita(id)
        );
    }


    // DELETE
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}