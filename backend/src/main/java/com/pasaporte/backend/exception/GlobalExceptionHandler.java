package com.pasaporte.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> manejarArgumentoInvalido(IllegalArgumentException ex) {
        Map<String, Object> cuerpoRespuesta = new LinkedHashMap<>();
        cuerpoRespuesta.put("timestamp", LocalDateTime.now());
        cuerpoRespuesta.put("status", HttpStatus.BAD_REQUEST.value());
        cuerpoRespuesta.put("error", "Petición Incorrecta");
        cuerpoRespuesta.put("message", ex.getMessage());

        return new ResponseEntity<>(cuerpoRespuesta, HttpStatus.BAD_REQUEST);
    }
}