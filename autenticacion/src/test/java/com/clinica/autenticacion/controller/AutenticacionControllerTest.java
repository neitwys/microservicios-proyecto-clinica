package com.clinica.autenticacion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.clinica.autenticacion.dto.LoginDTO;
import com.clinica.autenticacion.dto.UsuarioMostrarDTO;
import com.clinica.autenticacion.dto.UsuarioRegistroDTO;
import com.clinica.autenticacion.model.Usuario;
import com.clinica.autenticacion.service.UsuarioService;


@WebMvcTest(controllers = UsuarioController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService service;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules();
    }

    private UsuarioRegistroDTO crearRegistroDTOValido() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setRut("3-34443");
        dto.setNombre("DSADSA");
        dto.setEmail("saddsa@a.cl");
        dto.setClave("doctor123");
        dto.setRol("PACIENTE");
        dto.setEspecialidad("Neurocirugía");
        dto.setFechaNacimiento(LocalDate.of(1980, 5, 15));
        return dto;
    }

    private UsuarioMostrarDTO crearMostrarDTOValido(Integer id) {
        UsuarioMostrarDTO dto = new UsuarioMostrarDTO();
        dto.setId(id);
        dto.setRut("3-34443");
        dto.setNombre("DSADSA");
        dto.setEmail("saddsa@a.cl");
        dto.setRol("PACIENTE");
        dto.setEspecialidad("Neurocirugía");
        dto.setEstado(true);
        return dto;
    }

    private Usuario crearEntidadUsuario(Integer id) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setRut("3-34443");
        u.setNombre("DSADSA");
        u.setEmail("saddsa@a.cl");
        u.setClave("$2a$10$encryptedPasswordHere");
        u.setRol("PACIENTE");
        u.setEspecialidad("Neurocirugía");
        u.setFechaNacimiento(LocalDate.of(1980, 5, 15));
        u.setEstado(true);
        return u;
    }

    @Test
    void debeRegistrarUsuario() throws Exception {
        UsuarioRegistroDTO registroDTO = crearRegistroDTOValido();
        Usuario usuarioGuardado = crearEntidadUsuario(1);

        when(service.guardarUsuario(any(UsuarioRegistroDTO.class))).thenReturn(usuarioGuardado);

        mockMvc.perform(post("/autenticacion/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("saddsa@a.cl"));
    }

    @Test
    void debeHacerLoginExitoso() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("saddsa@a.cl");
        loginDTO.setClave("doctor123");

        Usuario usuario = crearEntidadUsuario(1);

        when(service.login("saddsa@a.cl", "doctor123")).thenReturn(usuario);

        mockMvc.perform(post("/autenticacion/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("saddsa@a.cl"));
    }

    @Test
    void debeRetornar401CuandoLoginFalla() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("incorrecto@a.cl");
        loginDTO.setClave("malaclave");

        when(service.login("incorrecto@a.cl", "malaclave"))
                .thenThrow(new IllegalArgumentException("Usuario no encontrado"));

        mockMvc.perform(post("/autenticacion/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void debeBuscarUsuarioPorId() throws Exception {
        UsuarioMostrarDTO mostrarDTO = crearMostrarDTOValido(1);

        when(service.buscarPorId(1)).thenReturn(mostrarDTO);

        mockMvc.perform(get("/autenticacion/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void debeRetornar404CuandoUsuarioNoExiste() throws Exception {
        when(service.buscarPorId(99)).thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(get("/autenticacion/buscar/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void debeListarUsuarios() throws Exception {
        when(service.listarUsuariosDTO()).thenReturn(List.of(crearMostrarDTOValido(1)));

        mockMvc.perform(get("/autenticacion/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void debeActualizarUsuario() throws Exception {
        UsuarioRegistroDTO registroDTO = crearRegistroDTOValido();
        Usuario usuarioActualizado = crearEntidadUsuario(1);
        usuarioActualizado.setNombre("Nombre Actualizado");

        when(service.actualizarUsuario(eq(1), any(UsuarioRegistroDTO.class))).thenReturn(usuarioActualizado);

        mockMvc.perform(put("/autenticacion/actualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nombre Actualizado"));
    }

    @Test
    void debeEliminarUsuario() throws Exception {
        doNothing().when(service).eliminarUsuario(1);

        mockMvc.perform(delete("/autenticacion/eliminar/1"))
                .andExpect(status().isNoContent());
    }
}