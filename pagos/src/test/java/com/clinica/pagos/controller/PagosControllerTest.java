package com.clinica.pagos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.clinica.pagos.dto.PagoCrearDTO;
import com.clinica.pagos.dto.PagoMostrarDTO;
import com.clinica.pagos.service.PagoService;

@WebMvcTest(controllers = PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PagoService service;

    private PagoCrearDTO crearInputDTO() {
        PagoCrearDTO dto = new PagoCrearDTO();
        dto.setIdCita(10);
        dto.setIdPaciente(5);
        dto.setMonto(45000.0);
        dto.setMetodoPago("TARJETA");
        return dto;
    }

    private PagoMostrarDTO crearMostrarDTO(Integer id) {
        PagoMostrarDTO dto = new PagoMostrarDTO();
        dto.setId(id);
        dto.setIdCita(10);
        dto.setIdPaciente(5);
        dto.setNombrePaciente("Juan Pérez");
        dto.setMonto(45000.0);
        dto.setMetodoPago("TARJETA");
        dto.setEstado("COMPLETADO");
        dto.setFechaCreacion(LocalDateTime.now());
        return dto;
    }

    @Test
    void debeListarPagos() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(crearMostrarDTO(1)));

        mockMvc.perform(get("/pagos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].monto").value(45000.0))
                .andExpect(jsonPath("$[0].nombrePaciente").value("Juan Pérez"));
    }

    @Test
    void debeBuscarPagoPorId() throws Exception {
        when(service.buscarPorId(1)).thenReturn(crearMostrarDTO(1));

        mockMvc.perform(get("/pagos/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    void debeCrearPago() throws Exception {
        PagoCrearDTO input = crearInputDTO();
        PagoMostrarDTO output = crearMostrarDTO(1);

        when(service.registrarPago(any(PagoCrearDTO.class))).thenReturn(output);

        mockMvc.perform(post("/pagos/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk()) // Coincide con tu ResponseEntity.ok()
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test
    void debeActualizarPago() throws Exception {
        PagoCrearDTO input = crearInputDTO();
        PagoMostrarDTO output = crearMostrarDTO(1);
        output.setMonto(50000.0);

        when(service.actualizarPago(eq(1), any(PagoCrearDTO.class))).thenReturn(output);

        mockMvc.perform(put("/pagos/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(50000.0));
    }

    @Test
    void debeEliminarPago() throws Exception {
        doNothing().when(service).eliminarPorId(1);

        mockMvc.perform(delete("/pagos/eliminar/1"))
                .andExpect(status().isOk()) // Coincide con tu ResponseEntity.ok() de texto plano
                .andExpect(content().string("Pago eliminado exitosamente con id 1"));
    }
}