package com.clinica.notificaciones.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.clinica.notificaciones.model.Notificacion;
import com.clinica.notificaciones.service.NotificacionService;

@WebMvcTest(controllers = NotificacionController.class})
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificacionService service;

    private Notificacion crearNotificacionValida(Integer id) {
        Notificacion n = new Notificacion();
        n.setId(id);
        n.setDestinatario("paciente@correo.cl");
        n.setMensaje("Su cita médica ha sido programada con éxito.");
        n.setTipo("EMAIL");
        n.setEstado("PENDIENTE");
        return n;
    }

    @Test
    void debeCrearNotificacion() throws Exception {
        Notificacion notificacionInput = crearNotificacionValida(null);
        Notificacion notificacionGuardada = crearNotificacionValida(1);

        when(service.registrarYEnviar(any(Notificacion.class))).thenReturn(notificacionGuardada);

        mockMvc.perform(post("/notificaciones/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificacionInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.destinatario").value("paciente@correo.cl"));
    }

    @Test
    void debeListarNotificaciones() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(crearNotificacionValida(1)));

        mockMvc.perform(get("/notificaciones/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipo").value("EMAIL"));
    }

    @Test
    void debeBuscarNotificacionPorId() throws Exception {
        Notificacion n = crearNotificacionValida(1);

        when(service.buscarPorId(1)).thenReturn(n);

        mockMvc.perform(get("/notificaciones/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.mensaje").value("Su cita médica ha sido programada con éxito."));
    }

    @Test
    void debeActualizarNotificacion() throws Exception {
        Notificacion input = crearNotificacionValida(1);
        input.setEstado("ENVIADO");

        when(service.actualizarNotificacion(eq(1), any(Notificacion.class))).thenReturn(input);

        mockMvc.perform(put("/notificaciones/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENVIADO"));
    }

    @Test
    void debeEliminarNotificacion() throws Exception {
        doNothing().when(service).eliminarNotificacion(1);

        mockMvc.perform(delete("/notificaciones/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}