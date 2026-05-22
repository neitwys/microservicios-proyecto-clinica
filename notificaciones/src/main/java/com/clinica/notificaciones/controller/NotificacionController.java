package com.clinica.notificaciones.controller;

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

import com.clinica.notificaciones.model.Notificacion;
import com.clinica.notificaciones.service.NotificacionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {
    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @PostMapping("/crear")
    public ResponseEntity<Notificacion> crearNotificacion(@Valid @RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(service.registrarYEnviar(notificacion));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Notificacion>> listarNotificaciones() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Notificacion> buscarNotificacion(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Notificacion> actualizarNotificacion(@PathVariable Integer id, @Valid @RequestBody Notificacion notificacion) {
        return ResponseEntity.ok(service.actualizarNotificacion(id, notificacion));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Integer id) {
        service.eliminarNotificacion(id);
        return ResponseEntity.status(204).build();
    }
}
