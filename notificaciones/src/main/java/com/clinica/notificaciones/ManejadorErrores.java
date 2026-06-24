package com.clinica.notificaciones;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clinica.notificaciones.dto.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManejadorErrores {
    //Manejo de errores de validación (cuando falla @Valid en un request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //Indica que este metodo se ejecuta automaticamente cuando ocurre una excepción de validación
    public ResponseEntity<ErrorDTO> manejarErroresValidacion(
        MethodArgumentNotValidException ex,     //Contiene el detalle de errores de validación
        HttpServletRequest request) {           //Permite obtener información del request (ej: la URL)

            //Mapa donde se almacenaran los errores por campo (ej: "nombre" -> "no puede estar vacio")
            Map<String, String> errores = new HashMap<>();

            ex.getBindingResult().getFieldErrors().forEach(error -> {   //Se recorren los errores detectados por Spring
                errores.put(error.getField(), error.getDefaultMessage());//Se guarda el nombre del campo y su mensaje en el mapa
            });

            //Se crea un objeto ErrorDTO con la información del error
            String mensajeGeneral = "Datos inválidos: revisa los campos requeridos";
            ErrorDTO errorDTO = new ErrorDTO(
                LocalDateTime.now(),
                400,
                mensajeGeneral,
                errores,
                request.getRequestURI()
            );

            //Se construye la respuesta HTTP:
            // - badRequest() -> Establece el estado HTTP 400
            // - body(errorDTO) -> envia el objeto errorDTO como respuesta en formato JSON
            return ResponseEntity.badRequest().body(errorDTO);
        }
    
    //Este es para manejar errores que son ilegales, como poner el mismo rut o correo al registrar.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> manejarArgumentosMalos(
        IllegalArgumentException ex, 
        HttpServletRequest request) {

        ErrorDTO errorDTO = new ErrorDTO(
            LocalDateTime.now(),
            400, // Bad Request
            ex.getMessage(), 
            null, 
            request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorDTO);
    }
}
