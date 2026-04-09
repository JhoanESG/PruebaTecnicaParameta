package com.parameta.empleados.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Error de negocio (mayoría de edad, etc.)
    @ExceptionHandler(EmpleadoException.class)
    public ResponseEntity<Map<String, Object>> handleEmpleadoException(EmpleadoException ex) {
        return ResponseEntity.unprocessableContent().body(Map.of(
                "status", 422,
                "error", ex.getMessage()
        ));
    }

    // Formato inválido: fechas y enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleFormatoInvalido(
            HttpMessageNotReadableException ex) {

        String mensaje = "Formato de solicitud inválido.";

        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {

            if (invalidFormat.getTargetType().isEnum()) {
                String valoresPermitidos = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                mensaje = "Tipo de documento inválido. Los valores permitidos son: " + valoresPermitidos;
            }

            else if (invalidFormat.getTargetType().equals(LocalDate.class)) {
                mensaje = "Formato de fecha inválido. Use el formato: yyyy-MM-dd";
            }
        }

        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "error", mensaje
        ));
    }
}
