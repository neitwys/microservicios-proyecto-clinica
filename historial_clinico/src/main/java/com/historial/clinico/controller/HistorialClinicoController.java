package com.historial.clinico.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.historial.clinico.dto.HistorialClinicoDTO;
import com.historial.clinico.dto.HistorialClinicoResponseDTO;
import com.historial.clinico.service.HistorialClinicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/historial")
@RequiredArgsConstructor
public class HistorialClinicoController {

    private final HistorialClinicoService service;

    @GetMapping("/listar")
    public List<HistorialClinicoResponseDTO> listar() {
        return service.listar();
    }

    @PostMapping("/crear")
    public ResponseEntity<HistorialClinicoResponseDTO> crear(@Valid @RequestBody HistorialClinicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping("/buscar/{id}")
    public HistorialClinicoResponseDTO buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/actualizar/{id}")
    public HistorialClinicoResponseDTO actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HistorialClinicoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Historial eliminado correctamente";
    }

    @GetMapping("/paciente/{idPaciente}")
    public List<HistorialClinicoResponseDTO> buscarPaciente(@PathVariable Long idPaciente) {
        return service.buscarPorPaciente(idPaciente);
    }

    @GetMapping("/fecha/{fechaConsulta}")
    public List<HistorialClinicoResponseDTO> buscarFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaConsulta) {
        return service.buscarPorFecha(fechaConsulta);
    }

    @GetMapping("/diagnostico/{diagnostico}")
    public List<HistorialClinicoResponseDTO> buscarDiagnostico(@PathVariable String diagnostico) {
        return service.buscarPorDiagnostico(diagnostico);
    }
}
