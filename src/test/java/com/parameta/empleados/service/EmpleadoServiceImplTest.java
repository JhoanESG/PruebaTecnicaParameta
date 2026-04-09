package com.parameta.empleados.service;

import com.parameta.empleados.dto.EmpleadoRequestDTO;
import com.parameta.empleados.dto.EmpleadoResponseDTO;
import com.parameta.empleados.exception.EmpleadoException;
import com.parameta.empleados.model.Empleado;
import com.parameta.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository repository;

    @InjectMocks
    private EmpleadoServiceImpl service;

    private EmpleadoRequestDTO dtoValido;
    private Empleado empleadoGuardado;

    @BeforeEach
    void setUp() {
        dtoValido = new EmpleadoRequestDTO();
        dtoValido.setNombres("Juan");
        dtoValido.setApellidos("Pérez");
        dtoValido.setTipoDocumento(Empleado.DocumentType.CC);
        dtoValido.setNumeroDocumento("123456789");
        dtoValido.setFechaNacimiento(LocalDate.of(1995, 8, 15));
        dtoValido.setFechaVinculacion(LocalDate.of(2020, 3, 1));
        dtoValido.setCargo("Desarrollador");
        dtoValido.setSalario(4000000.0);

        empleadoGuardado = new Empleado();
        empleadoGuardado.setId(1L);
        empleadoGuardado.setNombres(dtoValido.getNombres());
        empleadoGuardado.setApellidos(dtoValido.getApellidos());
        empleadoGuardado.setTipoDocumento(dtoValido.getTipoDocumento());
        empleadoGuardado.setNumeroDocumento(dtoValido.getNumeroDocumento());
        empleadoGuardado.setFechaNacimiento(dtoValido.getFechaNacimiento());
        empleadoGuardado.setFechaVinculacion(dtoValido.getFechaVinculacion());
        empleadoGuardado.setCargo(dtoValido.getCargo());
        empleadoGuardado.setSalario(dtoValido.getSalario());
    }

    // ─── registrarEmpleado ───────────────────────────────────────────────────

    @Test
    @DisplayName("Debe registrar exitosamente un empleado con datos válidos")
    void debeRegistrarEmpleadoExitosamente() {
        when(repository.existsByNumeroDocumento("123456789")).thenReturn(false);
        when(repository.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        EmpleadoResponseDTO response = service.registrarEmpleado(dtoValido);

        assertNotNull(response);
        assertEquals("Juan", response.getNombres());
        assertEquals("Pérez", response.getApellidos());
        assertNotNull(response.getEdadActual());
        assertNotNull(response.getTiempoVinculacion());
        assertNotNull(response.getMensajeBienvenida());
        assertTrue(response.getMensajeBienvenida().contains("Juan"));
        verify(repository, times(1)).save(any(Empleado.class));
    }

    @Test
    @DisplayName("Debe calcular correctamente la edad actual en la respuesta")
    void debeCalcularEdadCorrectamente() {
        when(repository.existsByNumeroDocumento(any())).thenReturn(false);
        when(repository.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        EmpleadoResponseDTO response = service.registrarEmpleado(dtoValido);

        // Verificamos que la edad tenga el formato esperado
        assertTrue(response.getEdadActual().contains("años"));
        assertTrue(response.getEdadActual().contains("meses"));
        assertTrue(response.getEdadActual().contains("días"));
    }

    @Test
    @DisplayName("Debe calcular correctamente el tiempo de vinculación en la respuesta")
    void debeCalcularTiempoVinculacionCorrectamente() {
        when(repository.existsByNumeroDocumento(any())).thenReturn(false);
        when(repository.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        EmpleadoResponseDTO response = service.registrarEmpleado(dtoValido);

        assertTrue(response.getTiempoVinculacion().contains("años"));
        assertTrue(response.getTiempoVinculacion().contains("meses"));
    }

    // ─── isOfLegalAge ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe lanzar excepción cuando el empleado tiene exactamente 17 años")
    void debeLanzarExcepcionCuandoTiene17Anios() {
        dtoValido.setFechaNacimiento(LocalDate.now().minusYears(17));

        EmpleadoException ex = assertThrows(EmpleadoException.class,
                () -> service.registrarEmpleado(dtoValido));

        assertEquals("El empleado debe ser mayor de edad (mínimo 18 años)", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el empleado acaba de nacer")
    void debeLanzarExcepcionCuandoEsRecienNacido() {
        dtoValido.setFechaNacimiento(LocalDate.now());

        EmpleadoException ex = assertThrows(EmpleadoException.class,
                () -> service.registrarEmpleado(dtoValido));

        assertEquals("El empleado debe ser mayor de edad (mínimo 18 años)", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe permitir registro cuando el empleado tiene exactamente 18 años")
    void debePermitirRegistroCuandoTieneExactamente18Anios() {
        dtoValido.setFechaNacimiento(LocalDate.now().minusYears(18));
        when(repository.existsByNumeroDocumento(any())).thenReturn(false);
        when(repository.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        assertDoesNotThrow(() -> service.registrarEmpleado(dtoValido));
    }

    // ─── isValidEmploymentDate ───────────────────────────────────────────────

    @Test
    @DisplayName("Debe lanzar excepción cuando la fecha de vinculación es futura")
    void debeLanzarExcepcionCuandoFechaVinculacionEsFutura() {
        dtoValido.setFechaVinculacion(LocalDate.now().plusDays(1));

        EmpleadoException ex = assertThrows(EmpleadoException.class,
                () -> service.registrarEmpleado(dtoValido));

        assertEquals("La fecha de vinculación no puede ser una fecha futura", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe permitir registro cuando la fecha de vinculación es hoy")
    void debePermitirRegistroCuandoFechaVinculacionEsHoy() {
        dtoValido.setFechaVinculacion(LocalDate.now());
        when(repository.existsByNumeroDocumento(any())).thenReturn(false);
        when(repository.save(any(Empleado.class))).thenReturn(empleadoGuardado);

        assertDoesNotThrow(() -> service.registrarEmpleado(dtoValido));
    }

    // ─── existDocumentNumber ─────────────────────────────────────────────────

    @Test
    @DisplayName("Debe lanzar excepción cuando el número de documento ya existe")
    void debeLanzarExcepcionCuandoDocumentoYaExiste() {
        when(repository.existsByNumeroDocumento("123456789")).thenReturn(true);

        EmpleadoException ex = assertThrows(EmpleadoException.class,
                () -> service.registrarEmpleado(dtoValido));

        assertEquals("Ya existe un empleado con ese número de documento", ex.getMessage());
        verify(repository, never()).save(any());
    }

    // ─── getAllEmpleados ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Debe retornar lista con todos los empleados")
    void debeRetornarListaDeEmpleados() {
        when(repository.findAll()).thenReturn(List.of(empleadoGuardado));

        List<EmpleadoResponseDTO> lista = service.getAllEmpleados();

        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Juan", lista.get(0).getNombres());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay empleados")
    void debeRetornarListaVaciaCuandoNoHayEmpleados() {
        when(repository.findAll()).thenReturn(List.of());

        List<EmpleadoResponseDTO> lista = service.getAllEmpleados();

        assertNotNull(lista);
        assertTrue(lista.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe mapear correctamente todos los campos en getAllEmpleados")
    void debeMappearCorrectamenteTodosLosCampos() {
        when(repository.findAll()).thenReturn(List.of(empleadoGuardado));

        List<EmpleadoResponseDTO> lista = service.getAllEmpleados();
        EmpleadoResponseDTO response = lista.get(0);

        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getNombres());
        assertEquals("Pérez", response.getApellidos());
        assertEquals(Empleado.DocumentType.CC, response.getTipoDocumento());
        assertEquals("123456789", response.getNumeroDocumento());
        assertEquals(4000000.0, response.getSalario());
        assertNotNull(response.getEdadActual());
        assertNotNull(response.getTiempoVinculacion());
    }
}
