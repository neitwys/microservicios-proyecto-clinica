package com.historial.clinico.controller;

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

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.historial.clinico.dto.HistorialClinicoDTO;
import com.historial.clinico.dto.HistorialClinicoResponseDTO;
import com.historial.clinico.service.HistorialClinicoService;

class HistorialClinicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistorialClinicoService service;

    private HistorialClinicoResponseDTO crearRespuestaValida(Long id, Long idPaciente) {
        LocalDate fecha = LocalDate.now().plusDays(1);

        return HistorialClinicoResponseDTO.builder()
                .id(id)
                .idPaciente(idPaciente)
                .nombrePaciente("Juan Perez")
                .fechaConsulta(fecha)
                .diagnostico("Gripe")
                .medicoResponsable("Dra. Lopez")
                .build();
    }

    private String jsonCrear(Long idPaciente) {
        LocalDate fecha = LocalDate.now().plusDays(1);

        return "{"
                + "\"idPaciente\":" + idPaciente + ","
                + "\"nombrePaciente\":\"Juan Perez\","
                + "\"fechaConsulta\":\"" + fecha + "\","
                + "\"diagnostico\":\"Gripe\","
                + "\"tratamiento\":\"Reposo y medicación\","
                + "\"observaciones\":\"Sin observaciones\","
                + "\"medicoResponsable\":\"Dra. Lopez\""
                + "}";
    }

    @Test
    @DisplayName("Debe listar historial clínico")
    void debeListar() throws Exception {
        when(service.listar()).thenReturn(List.of(crearRespuestaValida(1L, 10L)));

        mockMvc.perform(get("/historial/listar"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe crear historial clínico")
    void debeCrear() throws Exception {
        HistorialClinicoResponseDTO salida = crearRespuestaValida(1L, 10L);

        when(service.crear(any(HistorialClinicoDTO.class))).thenReturn(salida);

        mockMvc.perform(post("/historial/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCrear(10L)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idPaciente").value(10L));
    }

    @Test
    @DisplayName("Debe buscar historial clínico por id")
    void debeBuscarPorId() throws Exception {
        when(service.buscar(1L)).thenReturn(crearRespuestaValida(1L, 10L));

        mockMvc.perform(get("/historial/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Debe actualizar historial clínico")
    void debeActualizar() throws Exception {
        HistorialClinicoResponseDTO salida = crearRespuestaValida(1L, 10L);

        when(service.actualizar(eq(1L), any(HistorialClinicoDTO.class))).thenReturn(salida);

        mockMvc.perform(put("/historial/actualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCrear(10L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnostico").value("Gripe"));
    }

    @Test
    @DisplayName("Debe eliminar historial clínico")
    void debeEliminar() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/historial/eliminar/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar historial clínico por paciente")
    void debeBuscarPorPaciente() throws Exception {
        when(service.buscarPorPaciente(10L)).thenReturn(List.of(crearRespuestaValida(1L, 10L)));

        mockMvc.perform(get("/historial/paciente/10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar historial clínico por fecha")
    void debeBuscarPorFecha() throws Exception {
        LocalDate fecha = LocalDate.now().plusDays(1);

        when(service.buscarPorFecha(fecha)).thenReturn(List.of(crearRespuestaValida(1L, 10L)));

        mockMvc.perform(get("/historial/fecha/" + fecha))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe buscar historial clínico por diagnóstico")
    void debeBuscarPorDiagnostico() throws Exception {
        when(service.buscarPorDiagnostico("Gripe")).thenReturn(List.of(crearRespuestaValida(1L, 10L)));

        mockMvc.perform(get("/historial/diagnostico/Gripe"))
                .andExpect(status().isOk());
    }
}
