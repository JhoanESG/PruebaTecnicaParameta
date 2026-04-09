package com.parameta.empleados.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Errores de validación",
                "detalle", errores
        ));
    }

    // Formato de fecha inválido
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleFormatoFecha(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", "Formato de fecha inválido. Use: yyyy-MM-dd"
        ));
    }

    // Error de negocio (mayoría de edad, etc.)
    @ExceptionHandler(EmpleadoException.class)
    public ResponseEntity<Map<String, Object>> handleEmpleadoException(EmpleadoException ex) {
        return ResponseEntity.unprocessableContent().body(Map.of(
                "status", 422,
                "error", ex.getMessage()
        ));
    }


}
