# 👔 Empleados API

API REST desarrollada en **Java con Spring Boot** para el registro y consulta de empleados. El servicio valida los datos del empleado, verifica mayoría de edad y persiste la información en una base de datos MySQL. La respuesta incluye campos calculados como la edad actual y el tiempo de vinculación a la compañía.

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java | Lenguaje principal |
| Spring Boot | Framework principal |
| Spring Data JPA | Persistencia y acceso a datos |
| Spring Validation | Validación de campos con anotaciones |
| MySQL | Base de datos relacional |
| Docker / Docker Compose | Contenedorización del proyecto |
| Springdoc OpenAPI (Swagger) | Documentación interactiva de endpoints |
| JUnit 5 | Framework de pruebas unitarias |
| Mockito | Mocks para pruebas unitarias |
| Lombok | Reducción de código repetitivo |
| Maven | Gestión de dependencias y build |

---

## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/com/tuempresa/empleados/
│   │   ├── controller/
│   │   │   └── EmpleadoController.java       # Endpoints REST
│   │   ├── service/
│   │   │   ├── EmpleadoService.java          # Interfaz del servicio
│   │   │   └── EmpleadoServiceImpl.java      # Lógica de negocio
│   │   ├── repository/
│   │   │   └── EmpleadoRepository.java       # Acceso a datos (JPA)
│   │   ├── model/
│   │   │   └── Empleado.java                 # Entidad JPA
│   │   ├── dto/
│   │   │   ├── EmpleadoRequestDTO.java       # Objeto de entrada con validaciones
│   │   │   └── EmpleadoResponseDTO.java      # Objeto de respuesta enriquecido
│   │   ├── exception/
│   │   │   ├── EmpleadoException.java        # Excepción de negocio personalizada
│   │   │   └── GlobalExceptionHandler.java   # Manejo global de errores
│   │   └── EmpleadosApplication.java         # Clase principal
│   └── resources/
│       └── application.properties            # Configuración de la aplicación
└── test/
    └── java/com/tuempresa/empleados/
        └── service/
            └── EmpleadoServiceImplTest.java  # Pruebas unitarias del servicio
```

---

## ✅ Funcionalidades

- Registro de empleados con validación de campos obligatorios
- Validación de formato en fechas (`yyyy-MM-dd`)
- Validación de tipo de documento mediante `Enum` (`RC`, `TI`, `CC`, `CE`, `PEP`, `PP`, `NIT`)
- Validación de que nombres y apellidos solo contengan letras
- Validación de que el número de documento solo contenga dígitos
- Validación de mayoría de edad (mínimo 18 años)
- Validación de que la fecha de vinculación no sea una fecha futura
- Validación de documento duplicado en base de datos
- Cálculo automático de la **edad actual** del empleado (años, meses y días)
- Cálculo automático del **tiempo de vinculación** a la compañía (años y meses)
- Consulta de todos los empleados registrados
- Manejo global de errores con respuestas JSON estructuradas
- Documentación interactiva con Swagger UI

---

## 🚀 Cómo ejecutar el proyecto

### Prerrequisitos

Asegúrate de tener instalado:

- Docker y Docker Compose
- Git

No es necesario tener Java ni Maven instalados localmente, ya que Docker se encarga de gestionarlos.

### 1. Clonar el repositorio

```bash
git clone https://github.com/JhoanESG/PruebaTecnicaParameta.git
cd empleados
```

### 2. Configurar las variables de entorno

Copia el archivo de ejemplo y completa los valores:

```bash
cp .env.example .env
```

Abre el archivo `.env` y define las credenciales:

```env
MYSQL_ROOT_PASSWORD=tu_password_root
MYSQL_DATABASE=empleados_db
MYSQL_USERNAME=empleado_user
MYSQL_PASSWORD=tu_password
```

### 3. Levantar el proyecto

```bash
docker-compose up --build
```

Este comando realiza automáticamente los siguientes pasos:

1. Descarga las imágenes de MySQL y Java
2. Construye el JAR de la aplicación con Maven
3. Levanta el contenedor de MySQL y espera a que esté listo
4. Levanta el contenedor de la aplicación Spring Boot

La aplicación estará disponible en: `http://localhost:8080`

### 4. Detener el proyecto

```bash
docker-compose down
```

> Los datos de la base de datos se conservan aunque se detenga la ejecución y se den de baja los contenedores, 
> esto ocurre gracias a la creación de un al volumen de datos para MySQL. 
> Solo se pierden al ejecutar `docker-compose down -v`.

---

## 📖 Documentación de la API (Swagger)

Una vez que la aplicación esté corriendo, accede a la documentación interactiva:

```
http://localhost:8080/swagger-ui/index.html
```

Desde Swagger se puede explorar y probar todos los endpoints directamente en el navegador.

---

## 📡 Endpoints

### `POST /api/empleados` — Registrar empleado

Registra un nuevo empleado en el sistema.

**Request body:**

```json
{
  "nombres": "Juan Carlos",
  "apellidos": "Pérez Gómez",
  "tipoDocumento": "CC",
  "numeroDocumento": "123456789",
  "fechaNacimiento": "1995-08-15",
  "fechaVinculacion": "2020-03-01",
  "cargo": "Desarrollador Backend",
  "salario": 4000000.00
}
```

**Valores válidos para `tipoDocumento`:** `RC`, `TI`, `CC`, `CE`, `PEP`, `PP`, `NIT`

**Respuesta exitosa `201 Created`:**

```json
{
  "id": 1,
  "nombres": "Juan Carlos",
  "apellidos": "Pérez Gómez",
  "tipoDocumento": "CC",
  "numeroDocumento": "123456789",
  "fechaNacimiento": "1995-08-15",
  "fechaVinculacion": "2020-03-01",
  "cargo": "Desarrollador Backend",
  "salario": 4000000.0,
  "tiempoVinculacion": "5 años, 1 meses",
  "edadActual": "29 años, 7 meses, 25 días",
  "mensajeBienvenida": "Bienvenido/a, Juan Carlos. Llevas 5 años, 1 meses con nosotros."
}
```

---

### `GET /api/empleados` — Listar todos los empleados

Retorna la lista completa de empleados registrados.

**Respuesta exitosa `200 OK`:**

```json
[
  {
    "id": 1,
    "nombres": "Juan Carlos",
    "apellidos": "Pérez Gómez",
    "tipoDocumento": "CC",
    "numeroDocumento": "123456789",
    "fechaNacimiento": "1995-08-15",
    "fechaVinculacion": "2020-03-01",
    "cargo": "Desarrollador Backend",
    "salario": 3500000.0,
    "tiempoVinculacion": "5 años, 1 meses",
    "edadActual": "29 años, 7 meses, 25 días",
    "mensajeBienvenida": "Bienvenido/a, Juan Carlos. Llevas 5 años, 1 meses con nosotros."
  }
]
```
---

## ⚠️ Manejo de errores

La API responde con mensajes de error estructurados en JSON para todos los casos de fallo.

**Errores de validación `400`:**

```json
{
  "status": 400,
  "error": "Errores de validación",
  "detalle": {
    "nombres": "Los nombres solo pueden contener letras y espacios",
    "numeroDocumento": "El número de documento solo puede contener dígitos"
  }
}
```

**Formato de fecha inválido `400`:**

```json
{
  "status": 400,
  "error": "Formato de fecha inválido. Use el formato: yyyy-MM-dd"
}
```

**Tipo de documento inválido `400`:**

```json
{
  "status": 400,
  "error": "Tipo de documento inválido. Los valores permitidos son: CC, CE, TI, PASAPORTE"
}
```

**Error de negocio `422`:**

```json
{
  "status": 422,
  "error": "El empleado debe ser mayor de edad (mínimo 18 años)"
}
```

---

## 🧪 Pruebas unitarias

Las pruebas unitarias cubren toda la lógica de negocio del servicio usando **JUnit 5** y **Mockito**.

Para ejecutarlas localmente (requiere Java y Maven instalados):

```bash
mvn test
```

**Casos de prueba incluidos:**

- Registro exitoso de empleado con datos válidos
- Cálculo correcto de edad actual
- Cálculo correcto de tiempo de vinculación
- Excepción al registrar empleado menor de edad (17 años, recién nacido)
- Límite: empleado con exactamente 18 años (debe permitirse)
- Excepción al usar fecha de vinculación futura
- Límite: fecha de vinculación en el día de hoy (debe permitirse)
- Excepción al registrar documento duplicado
- Listado de todos los empleados
- Lista vacía cuando no hay empleados registrados
- Mapeo correcto de todos los campos en la respuesta

---

## 📐 Decisiones de diseño

**Patrón Service + ServiceImpl:** Se usa la separación entre interfaz (`EmpleadoService`) e implementación (`EmpleadoServiceImpl`) siguiendo el principio de programación hacia interfaces, estándar en proyectos Spring empresariales.

**Principio de responsabilidad única:** El método principal del servicio delega en métodos privados especializados (`isOfLegalAge`, `isValidEmploymentDate`, `existDocumentNumber`, `mapToEntity`, `mapToResponseDTO`), cada uno con una única responsabilidad.

**DTOs separados para request y response:** El `EmpleadoRequestDTO` contiene las validaciones de entrada y el `EmpleadoResponseDTO` contiene los campos calculados, evitando exponer directamente la entidad JPA.

**Manejo global de excepciones:** El `GlobalExceptionHandler` centraliza todos los errores en un solo lugar, devolviendo siempre respuestas JSON consistentes independientemente del tipo de error.

**Multi-stage Docker build:** El `Dockerfile` usa dos etapas — una para compilar con Maven y otra solo con el JAR resultante — produciendo una imagen final más liviana y sin exponer el código fuente ni Maven en producción.

---

## 👤 Autor
### Jhoan Esteban Soler Giraldo

Desarrollado como prueba técnica para la vacante de Desarrollador Junior en la organización $Parameta$.