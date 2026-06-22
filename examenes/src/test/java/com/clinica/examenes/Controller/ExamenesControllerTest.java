package com.clinica.examenes.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.clinica.examenes.controller.ExamenesController;
import com.clinica.examenes.dto.ExamenDetalleDTO;
import com.clinica.examenes.model.Examenes;
import com.clinica.examenes.service.ExamenesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ExamenesController.class)
class ExamenesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExamenesService service;

    @Test
    void debeListarExamenes() throws Exception {

        Examenes examen = new Examenes(
                1,
                "Hemograma",
                "Sangre",
                LocalDate.now().plusDays(1),
                "Normal",
                "Completado",
                "correo@test.cl",
                1,
                2
        );

        when(service.listar())
                .thenReturn(List.of(examen));

        mockMvc.perform(get("/examenes/listar"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarExamenPorId() throws Exception {

        ExamenDetalleDTO dto = new ExamenDetalleDTO();
        dto.setId(1);
        dto.setNombreExamen("Hemograma");

        when(service.obtenerExamenPorId(1))
                .thenReturn(dto);

        mockMvc.perform(get("/examenes/buscar/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarExamenesPorPaciente() throws Exception {

        ExamenDetalleDTO dto = new ExamenDetalleDTO();
        dto.setId(1);
        dto.setNombrePaciente("Juan Pérez");

        when(service.obtenerExamenDTO(1))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/examenes/paciente/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeCrearExamen() throws Exception {

        Examenes examen = new Examenes(
                1,
                "Hemograma",
                "Sangre",
                LocalDate.now().plusDays(1),
                "Normal",
                "Pendiente",
                "correo@test.cl",
                1,
                2
        );

        when(service.guardarExamenes(any(Examenes.class)))
                .thenReturn(examen);

        mockMvc.perform(post("/examenes/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examen)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeActualizarExamen() throws Exception {

        Examenes examen = new Examenes(
                1,
                "Perfil Lipídico",
                "Sangre",
                LocalDate.now().plusDays(1),
                "Normal",
                "Completado",
                "correo@test.cl",
                1,
                2
        );

        when(service.actualizar(any(Integer.class), any(Examenes.class)))
                .thenReturn(examen);

        mockMvc.perform(put("/examenes/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(examen)))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarExamen() throws Exception {

        doNothing().when(service).eliminar(1);

        mockMvc.perform(delete("/examenes/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}
