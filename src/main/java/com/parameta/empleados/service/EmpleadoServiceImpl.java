package com.parameta.empleados.service;

import com.parameta.empleados.dto.*;
import com.parameta.empleados.exception.EmpleadoException;
import com.parameta.empleados.model.Empleado;
import com.parameta.empleados.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository repository;

    @Override
    public EmpleadoResponseDTO registrarEmpleado(EmpleadoRequestDTO dto) {

        isOfLegalAge(dto.getFechaNacimiento());
        isValidEmploymentDate(dto.getFechaVinculacion());
        existDocumentNumber(dto.getNumeroDocumento());

        Empleado empleado = mapToEntity(dto);
        Empleado guardado = repository.save(empleado);

        return mapToResponseDTO(guardado);
    }

    @Override
    public List<EmpleadoResponseDTO> getAllEmpleados() {
        return repository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    private void isOfLegalAge(LocalDate fechaNacimiento) {
        LocalDate hoy = LocalDate.now();
        Period edadPeriod = Period.between(fechaNacimiento, hoy);

        if (edadPeriod.getYears() < 18) {
            throw new EmpleadoException("El empleado debe ser mayor de edad (mínimo 18 años)");
        }
    }

    private void isValidEmploymentDate (LocalDate fechaVinculacion) {
        LocalDate hoy = LocalDate.now();

        if (fechaVinculacion.isAfter(hoy)) {
            throw new EmpleadoException("La fecha de vinculación no puede ser una fecha futura");
        }
    }

    private void existDocumentNumber (String numeroDocumento) {
        if (repository.existsByNumeroDocumento(numeroDocumento)) {
            throw new EmpleadoException("Ya existe un empleado con ese número de documento");
        }
    }

    private Empleado mapToEntity(EmpleadoRequestDTO dto) {
        Empleado empleado = new Empleado();
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setTipoDocumento(dto.getTipoDocumento());
        empleado.setNumeroDocumento(dto.getNumeroDocumento());
        empleado.setFechaNacimiento(dto.getFechaNacimiento());
        empleado.setFechaVinculacion(dto.getFechaVinculacion());
        empleado.setCargo(dto.getCargo());
        empleado.setSalario(dto.getSalario());
        return empleado;
    }

    private EmpleadoResponseDTO mapToResponseDTO(Empleado empleado) {
        //Calcular la edad y el tiempo de vinculacion
        LocalDate hoy = LocalDate.now();
        Period edadPeriod = Period.between(empleado.getFechaNacimiento(), hoy);
        Period vinculacion = Period.between(empleado.getFechaVinculacion(), hoy);

        //Estructurar los String para la respuesta
        String tiempoVinculacion = vinculacion.getYears()
                + " años, " + vinculacion.getMonths() + " meses";
        String edadActual = edadPeriod.getYears() + " años, "
                + edadPeriod.getMonths() + " meses, "
                + edadPeriod.getDays() + " días";

        //Construir la respuesta
        return EmpleadoResponseDTO.builder()
                .id(empleado.getId())
                .nombres(empleado.getNombres())
                .apellidos(empleado.getApellidos())
                .tipoDocumento(empleado.getTipoDocumento())
                .numeroDocumento(empleado.getNumeroDocumento())
                .fechaNacimiento(empleado.getFechaNacimiento().toString())
                .fechaVinculacion(empleado.getFechaVinculacion().toString())
                .cargo(empleado.getCargo())
                .salario(empleado.getSalario())
                .tiempoVinculacion(tiempoVinculacion)
                .edadActual(edadActual)
                .mensajeBienvenida("Bienvenido/a, " + empleado.getNombres()
                        + ". Llevas " + tiempoVinculacion + " con nosotros.")
                .build();
    }
}
