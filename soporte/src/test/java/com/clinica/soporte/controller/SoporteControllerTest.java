package com.clinica.soporte.controller;

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

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.clinica.soporte.model.Cita;
import com.clinica.soporte.model.Soporte;
import com.clinica.soporte.service.SoporteService;

class SoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SoporteService soporteService;

    private Soporte crearSoporteValido(Long id) {
        Soporte s = new Soporte();
        s.setId(id);
        s.setUsuario("usuario1");
        s.setModulo("CITAS");
        s.setDescripcion("Error al procesar cita");
        s.setEstado("Abierto");
        s.setPrioridad("GRAVE");
        s.setCorreo("usuario1@correo.cl");
        s.setCitaId(5L);
        s.setMensaje("Fallo");
        s.setResultados("Sin resultados");
        s.setFechaCreacion(LocalDateTime.now());
        return s;
    }

    private Cita crearCitaValida() {
        Cita c = new Cita();
        c.setId(123L);
        c.setPaciente("Juan Perez");
        c.setFecha("2026-01-01");
        c.setEstado("CREADA");
        c.setObservacion("Sin observaciones");
        return c;
    }

    private String jsonSoporte(Long citaId) {
        return "{"
                + "\"usuario\":\"usuario1\","
                + "\"modulo\":\"CITAS\","
                + "\"descripcion\":\"Error al procesar cita\","
                + "\"estado\":\"Abierto\","
                + "\"prioridad\":\"GRAVE\","
                + "\"correo\":\"usuario1@correo.cl\","
                + "\"citaId\":" + citaId + ","
                + "\"mensaje\":\"Fallo\","
                + "\"resultados\":\"Sin resultados\","
                + "\"fechaCreacion\":\"2026-01-01T10:00:00\""
                + "}";
    }

    @Test
    void debeListarSoportes() throws Exception {
        when(soporteService.listar()).thenReturn(List.of(crearSoporteValido(1L)));

        mockMvc.perform(get("/soporte/listar"))
                .andExpect(status().isOk());
    }

    @Test
    void debeCrearSoporte() throws Exception {
        Soporte salida = crearSoporteValido(1L);

        when(soporteService.guardar(any(Soporte.class))).thenReturn(salida);

        mockMvc.perform(post("/soporte/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSoporte(5L)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.usuario").value("usuario1"));
    }

    @Test
    void debeBuscarSoportePorId() throws Exception {
        when(soporteService.buscarPorId(1L)).thenReturn(crearSoporteValido(1L));

        mockMvc.perform(get("/soporte/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void debeActualizarSoporte() throws Exception {
        Soporte salida = crearSoporteValido(1L);
        salida.setEstado("Actualizado");

        when(soporteService.actualizar(eq(1L), any(Soporte.class))).thenReturn(salida);

        mockMvc.perform(put("/soporte/actualizar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSoporte(5L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Actualizado"));
    }

    @Test
    void debeEliminarSoporte() throws Exception {
        doNothing().when(soporteService).eliminarPorId(1L);

        mockMvc.perform(delete("/soporte/eliminar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeObtenerCita() throws Exception {
        when(soporteService.obtenerCita(1L)).thenReturn(crearCitaValida());

        mockMvc.perform(get("/soporte/cita/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeVerificarCita() throws Exception {
        when(soporteService.verificarCita(1L)).thenReturn(crearCitaValida());

        mockMvc.perform(get("/soporte/verificar-cita/1"))
                .andExpect(status().isOk());
    }

    @Test
    void debeCrearTicketCorreo() throws Exception {
        Soporte salida = crearSoporteValido(1L);
        salida.setEstado("CORREO_ENVIADO");

        when(soporteService.enviarTicketCorreo(any(Soporte.class))).thenReturn(salida);

        mockMvc.perform(post("/soporte/ticket-correo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSoporte(5L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CORREO_ENVIADO"));
    }

    @Test
    void debeAgregarResultados() throws Exception {
        Soporte salida = crearSoporteValido(1L);
        salida.setResultados("Resultados disponibles");
        salida.setEstado("RESULTADOS_AGREGADOS");

        when(soporteService.agregarResultados(any(Soporte.class))).thenReturn(salida);

        mockMvc.perform(post("/soporte/ticket-resultados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSoporte(5L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESULTADOS_AGREGADOS"));
    }
}
