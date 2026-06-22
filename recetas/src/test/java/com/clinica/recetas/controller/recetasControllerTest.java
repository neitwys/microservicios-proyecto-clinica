package com.clinica.recetas.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.clinica.recetas.DTO.RecetaCrearDTO;
import com.clinica.recetas.DTO.RecetaDetalleDTO;
import com.clinica.recetas.DTO.RecetaMostrarDTO;
import com.clinica.recetas.service.RecetasService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RecetaController.class)
class RecetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecetasService service;

    @Test
    void debeListarDetallePorPaciente() throws Exception {

        RecetaDetalleDTO dto = new RecetaDetalleDTO();
        dto.setIdReceta(1);
        dto.setNombrePaciente("Juan Pérez");

        when(service.listarDetallePaciente(1))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/recetas/detalle/paciente/1"))
                .andExpect(status().isOk());
    }

  
    @Test
    void debeCrearReceta() throws Exception {

        RecetaCrearDTO dto = RecetaCrearDTO.builder()
                .idCita(1)
                .idPaciente(1)
                .idMedico(2)
                .medicamento("Paracetamol")
                .indicaciones("Cada 8 horas")
                .fechaEmision(LocalDate.now())
                .duracionDias(5)
                .build();

        RecetaMostrarDTO respuesta = new RecetaMostrarDTO();

        when(service.crearReceta(any(RecetaCrearDTO.class)))
                .thenReturn(respuesta);

        mockMvc.perform(post("/recetas/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }


    @Test
    void debeListarRecetas() throws Exception {

        when(service.listarTodas())
                .thenReturn(List.of(new RecetaMostrarDTO()));

        mockMvc.perform(get("/recetas/listar"))
                .andExpect(status().isOk());
    }


    @Test
    void debeBuscarPorId() throws Exception {

        RecetaMostrarDTO dto = new RecetaMostrarDTO();

        when(service.buscarPorId(1))
                .thenReturn(dto);

        mockMvc.perform(get("/recetas/buscar/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeListarPorPaciente() throws Exception {

        when(service.listarPorPaciente(1))
                .thenReturn(List.of(new RecetaMostrarDTO()));

        mockMvc.perform(get("/recetas/paciente/1"))
                .andExpect(status().isOk());
    }


    @Test
    void debeListarPorMedico() throws Exception {

        when(service.listarPorMedico(1))
                .thenReturn(List.of(new RecetaMostrarDTO()));

        mockMvc.perform(get("/recetas/medico/1"))
                .andExpect(status().isOk());
    }

   
    @Test
    void debeEliminarReceta() throws Exception {

        RecetaMostrarDTO receta = new RecetaMostrarDTO();

        when(service.buscarPorId(1)).thenReturn(receta);
        doNothing().when(service).eliminarPorId(1);

        mockMvc.perform(delete("/recetas/eliminar/1"))
                .andExpect(status().isOk());
    }

  
    @Test
    void debeActualizarReceta() throws Exception {

        RecetaCrearDTO dto = RecetaCrearDTO.builder()
                .idCita(1)
                .idPaciente(1)
                .idMedico(2)
                .medicamento("Ibuprofeno")
                .indicaciones("Cada 12 horas")
                .fechaEmision(LocalDate.now())
                .duracionDias(3)
                .build();

        RecetaMostrarDTO respuesta = new RecetaMostrarDTO();

        when(service.actualizarReceta(any(Integer.class), any(RecetaCrearDTO.class)))
                .thenReturn(respuesta);

        mockMvc.perform(put("/recetas/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}