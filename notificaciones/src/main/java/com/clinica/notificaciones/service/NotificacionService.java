package com.clinica.notificaciones.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinica.notificaciones.model.Notificacion;
import com.clinica.notificaciones.repository.NotificacionRepository;

@Service
public class NotificacionService {
    private final NotificacionRepository repository;

    public NotificacionService(NotificacionRepository repository) {
        this.repository = repository;
    }

    //Metodo principal
    public Notificacion registrarYEnviar(Notificacion notificacion) {
        // 1. Lógica de "Envío" (Simulada por consola)
        System.out.println("======= SIMULACIÓN DE ENVÍO DE NOTIFICACIÓN =======");
        System.out.println("TIPO: " + notificacion.getTipo());
        System.out.println("DESTINATARIO: " + notificacion.getDestinatario());
        System.out.println("MENSAJE: " + notificacion.getMensaje());
        System.out.println("====================================================");
        
        // 2. Marcamos como "PENDIENTE" para simular que se envió pero aún no se confirma
        notificacion.setEstado("PENDIENTE");

        // 3. Guardamos en la base de datos de Notificaciones para el historial
        return repository.save(notificacion);
    }

    public List<Notificacion> listarTodos() {
        return repository.findAll();
    }

    public Notificacion buscarPorId(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con id " + id));
    }

    public Notificacion actualizarNotificacion(Integer id, Notificacion notificacion) {
        Notificacion existente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con id " + id));
        
        existente.setDestinatario(notificacion.getDestinatario());
        existente.setMensaje(notificacion.getMensaje());
        existente.setTipo(notificacion.getTipo());
        existente.setEstado(notificacion.getEstado());
        
        return repository.save(existente);
    }

    public void eliminarNotificacion(Integer id) {
        Notificacion existente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con id " + id));
        
        repository.delete(existente);
    }



}
