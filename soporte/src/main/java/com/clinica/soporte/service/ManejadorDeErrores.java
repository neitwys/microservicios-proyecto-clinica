package com.clinica.soporte.service;

import com.clinica.soporte.dto.ErrorDTO;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ManejadorDeErrores {

    public ErrorDTO createValidationError(String path, Map<String, String> errors) {
        return new ErrorDTO(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            errors,
            path
        );
    }

    public ErrorDTO createError(String path, HttpStatus status, String message) {
        return new ErrorDTO(
            LocalDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message,
            path
        );
    }
}
