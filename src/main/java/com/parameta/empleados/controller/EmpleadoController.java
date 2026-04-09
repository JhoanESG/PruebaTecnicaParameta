package com.parameta.empleados.controller;

import com.parameta.empleados.dto.*;
import com.parameta.empleados.service.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService service;

    @PostMapping("/registrar")
    public ResponseEntity<EmpleadoResponseDTO> registrar(@Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarEmpleado(dto));
    }

    @GetMapping("/consultar")
    public ResponseEntity<List<EmpleadoResponseDTO>> consultar() {
        return ResponseEntity.ok(service.getAllEmpleados());
    }
}
