package com.parameta.empleados.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EmpleadoRequestDTO {

    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El tipo de documento es obligatorio")
    private String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    private String numeroDocumento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de vinculación es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVinculacion;

    @NotBlank(message = "El cargo es obligatorio")
    private String cargo;

    @NotNull(message = "El salario es obligatorio")
    @Positive(message = "El salario debe ser un valor positivo")
    private Double salario;
}
