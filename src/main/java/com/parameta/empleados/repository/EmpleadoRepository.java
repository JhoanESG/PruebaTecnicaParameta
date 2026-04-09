package com.parameta.empleados.repository;

import com.parameta.empleados.model.Empleado;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado,Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);
}
