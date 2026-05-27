package com.clinica.soporte.controller;

import com.clinica.soporte.model.Cita;
import com.clinica.soporte.model.Soporte;
import com.clinica.soporte.service.SoporteService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/soporte")
public class SoporteController {

    private final SoporteService soporteService;

    public SoporteController(SoporteService soporteService) {
        this.soporteService = soporteService;
    }

    @GetMapping("/listar")
    public List<Soporte> listar() {
        return soporteService.listar();
    }

    @PostMapping("/crear")
    public ResponseEntity<Soporte> crear(@Valid @RequestBody Soporte request) {
        Soporte soporte = soporteService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(soporte);
    }

    @GetMapping("/buscar/{id}")
    public Soporte buscar(@PathVariable Long id) {
        return soporteService.buscarPorId(id);
    }

    @PutMapping("/actualizar/{id}")
    public Soporte actualizar(@PathVariable Long id, @Valid @RequestBody Soporte request) {
        return soporteService.actualizar(id, request);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        soporteService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cita/{id}")
    public Cita cita(@PathVariable Long id) {
        return soporteService.obtenerCita(id);
    }

    @GetMapping("/verificar-cita/{id}")
    public Cita verificarCita(@PathVariable Long id) {
        return soporteService.verificarCita(id);
    }

    @PostMapping("/ticket-correo")
    public Soporte ticketCorreo(@Valid @RequestBody Soporte request) {
        return soporteService.enviarTicketCorreo(request);
    }

    @PostMapping("/ticket-resultados")
    public Soporte ticketResultados(@Valid @RequestBody Soporte request) {
        return soporteService.agregarResultados(request);
    }
}
