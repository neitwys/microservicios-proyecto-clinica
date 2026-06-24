package com.clinica.autenticacion.controller;

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

import com.clinica.autenticacion.dto.LoginDTO;
import com.clinica.autenticacion.dto.UsuarioMostrarDTO;
import com.clinica.autenticacion.dto.UsuarioRegistroDTO;
import com.clinica.autenticacion.model.Usuario;
import com.clinica.autenticacion.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/autenticacion")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(
        summary = "Crear usuario",
        description = "Registra un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<UsuarioMostrarDTO> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
        Usuario nuevoUsuario = service.guardarUsuario(usuarioRegistroDTO);

        UsuarioMostrarDTO respuestaDTO = new UsuarioMostrarDTO();

        respuestaDTO.setId(nuevoUsuario.getId());
        respuestaDTO.setRut(nuevoUsuario.getRut());
        respuestaDTO.setNombre(nuevoUsuario.getNombre());
        respuestaDTO.setEmail(nuevoUsuario.getEmail());
        respuestaDTO.setRol(nuevoUsuario.getRol());
        respuestaDTO.setEspecialidad(nuevoUsuario.getEspecialidad());
        respuestaDTO.setEstado(nuevoUsuario.getEstado());

        return ResponseEntity.status(201).body(respuestaDTO);
    }

    @Operation(
        summary = "Login de usuario",
        description = "Inicia sesión con un usuario existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = service.login(loginDTO.getEmail(), loginDTO.getClave());
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @Operation(
        summary = "Buscar usuario por ID",
        description = "Obtiene un usuario específico mediante su identificador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioMostrarDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            UsuarioMostrarDTO usuario = service.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(
        summary = "Listar usuarios",
        description = "Obtiene todos los usuarios registrados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioMostrarDTO>> listar() {
        return ResponseEntity.ok(service.listarUsuariosDTO());
    }

    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza los datos de un usuario existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioMostrarDTO> actualizar(@PathVariable Integer id, @RequestBody UsuarioRegistroDTO dto) {
        try {
            // 1. El service hace la pega de buscar y guardar
            Usuario actualizado = service.actualizarUsuario(id, dto);
            
            // 2. Mapeas al DTO de salida (lo mismo de siempre)
            UsuarioMostrarDTO respuesta = new UsuarioMostrarDTO();
            respuesta.setId(actualizado.getId());
            respuesta.setNombre(actualizado.getNombre());
            respuesta.setEmail(actualizado.getEmail());
            respuesta.setRut(actualizado.getRut());
            respuesta.setRol(actualizado.getRol());
            respuesta.setEspecialidad(actualizado.getEspecialidad());
            respuesta.setEstado(actualizado.getEstado());

            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(
        summary = "Eliminar usuario",
        description = "Elimina un usuario del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarUsuario(id);
        return ResponseEntity.status(204).build();
    }
}
