package com.mvclinicas.Citas.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarRuntimeException(RuntimeException ex) {
        // Captura cualquier RuntimeException del servicio y la manda como un 400 limpio
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}