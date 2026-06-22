package com.mvclinicas.Citas.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvclinicas.Citas.model.Citas;
import com.mvclinicas.Citas.service.CitasService;

@WebMvcTest(CitasController.class)
class CitasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CitasService service;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules(); // FIX LocalDate / LocalTime
    }

    private Citas crearCitaValida() {
        Citas cita = new Citas();
        cita.setIdCita(1);
        cita.setIdPaciente(1);
        cita.setIdMedico(2);
        cita.setFecha(LocalDate.now().plusDays(1));
        cita.setHora(LocalTime.of(10, 30));
        cita.setMotivo("Control médico");
        cita.setEstado("CREADA");
        cita.setIdPago(null);
        return cita;
    }

    @Test
    void debeCrearCita() throws Exception {

        Citas cita = crearCitaValida();

        when(service.crearCita(any(Citas.class)))
                .thenReturn(cita);

        mockMvc.perform(post("/citas/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeListarCitas() throws Exception {

        when(service.listar())
                .thenReturn(List.of(crearCitaValida()));

        mockMvc.perform(get("/citas/listar"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarCitaPorId() throws Exception {

        when(service.obtenerPorId(1))
                .thenReturn(crearCitaValida());

        mockMvc.perform(get("/citas/buscar/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarPorPaciente() throws Exception {

        when(service.listarPorPaciente(1))
                .thenReturn(List.of(crearCitaValida()));

        mockMvc.perform(get("/citas/paciente/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarPorMedico() throws Exception {

        when(service.listarPorMedico(1))
                .thenReturn(List.of(crearCitaValida()));

        mockMvc.perform(get("/citas/medico/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeActualizarCita() throws Exception {

        Citas cita = crearCitaValida();

        when(service.actualizar(any(Integer.class), any(Citas.class)))
                .thenReturn(cita);

        mockMvc.perform(put("/citas/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isOk());
    }

    @Test
    void debeConfirmarCita() throws Exception {

        Citas cita = crearCitaValida();

        when(service.confirmarCita(1))
                .thenReturn(cita);

        mockMvc.perform(put("/citas/1/confirmar"))
                .andExpect(status().isOk());
    }

    @Test
    void debeCancelarCita() throws Exception {

        Citas cita = crearCitaValida();

        when(service.cancelarCita(1))
                .thenReturn(cita);

        mockMvc.perform(put("/citas/1/cancelar"))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarCita() throws Exception {

        doNothing().when(service).eliminar(1);

        mockMvc.perform(delete("/citas/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}