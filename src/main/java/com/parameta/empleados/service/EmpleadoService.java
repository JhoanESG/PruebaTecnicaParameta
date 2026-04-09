package com.parameta.empleados.service;

import com.parameta.empleados.dto.*;

import java.util.List;

public interface EmpleadoService {

    EmpleadoResponseDTO registrarEmpleado(EmpleadoRequestDTO dto);

    List<EmpleadoResponseDTO> getAllEmpleados();
}
