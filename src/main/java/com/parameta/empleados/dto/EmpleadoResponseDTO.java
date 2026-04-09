package com.parameta.empleados.dto;

import com.parameta.empleados.model.Empleado;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmpleadoResponseDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private Empleado.DocumentType tipoDocumento;
    private String numeroDocumento;
    private String fechaNacimiento;
    private String fechaVinculacion;
    private String cargo;
    private Double salario;

    // Campos calculados (requeridos)
    private String tiempoVinculacion;  // "X años, Y meses"
    private String edadActual;         // "X años, Y meses, Z días"

    // Agregado
    private String mensajeBienvenida;
}
