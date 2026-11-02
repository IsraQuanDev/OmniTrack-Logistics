# 🚛 OmniTrack Logistics
## Plataforma de Seguimiento Logístico y Envíos (Fleet & Asset Tracking)
### Microservicios Reactivos de Categoría 3 (Punto 2)

[![CI/CD Build & Tests](https://github.com/IsraQuanDev/OmniTrack-Logistics/actions/workflows/ci.yml/badge.svg)](https://github.com/IsraQuanDev/OmniTrack-Logistics/actions)
[![Java 17](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Reactive-Spring%20WebFlux-orange.svg)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![R2DBC](https://img.shields.io/badge/Data-R2DBC%20Reactive-red.svg)](https://r2dbc.io/)
[![Unit Tests](https://img.shields.io/badge/Unit%20Tests-46%2F46%20Passing-success.svg)](https://github.com/IsraQuanDev/OmniTrack-Logistics)
[![Docker](https://img.shields.io/badge/Docker%20Compose-Ready-blue.svg)](https://www.docker.com/)
[![PostgreSQL PostGIS](https://img.shields.io/badge/PostGIS-15--3.3-blue.svg)](https://postgis.net/)
[![Redis](https://img.shields.io/badge/Redis-7.0-red.svg)](https://redis.io/)
[![OpenAPI 3](https://img.shields.io/badge/OpenAPI-Swagger%20UI-green.svg)](https://swagger.io/)
[![License MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🌐 Entorno de Pruebas en Vivo (Despliegue Público Activo)

Los microservicios cuentan con **túneles seguros HTTPS de Cloudflare activos en tiempo real**, lo que permite a evaluadores, revisores y usuarios probar las APIs y Swagger UI interactivamente sin necesidad de clonar ni compilar el proyecto:

| Microservicio | URL Base Pública | Documentación Interactiva Swagger UI | Monitoreo Actuator |
|---|---|---|---|
| **Fleet Management Service** | `https://sat-agriculture-shoes-affiliate.trycloudflare.com` | [Swagger UI Fleet](https://sat-agriculture-shoes-affiliate.trycloudflare.com/swagger-ui.html) | [Health](https://sat-agriculture-shoes-affiliate.trycloudflare.com/actuator/health) |
| **GPS Ingestion Service** | `https://ping-early-parish-track.trycloudflare.com` | [Swagger UI GPS](https://ping-early-parish-track.trycloudflare.com/swagger-ui.html) | [Health](https://ping-early-parish-track.trycloudflare.com/actuator/health) |
| **Route Calculation Service** | `https://months-businesses-mins-gibson.trycloudflare.com` | [Swagger UI Routes](https://months-businesses-mins-gibson.trycloudflare.com/swagger-ui.html) | [Health](https://months-businesses-mins-gibson.trycloudflare.com/actuator/health) |
| **Alert Service** | `https://prime-nil-rivers-bundle.trycloudflare.com` | [Swagger UI Alerts](https://prime-nil-rivers-bundle.trycloudflare.com/swagger-ui.html) | [Health](https://prime-nil-rivers-bundle.trycloudflare.com/actuator/health) |

> 💡 **Credenciales de prueba**: Usuario: `user` \| Contraseña: `user123` (o Administrador: `admin` / `admin123`).

---

## 📋 Resumen Ejecutivo

**OmniTrack Logistics** es una solución empresarial de alto rendimiento basada en **microservicios reactivos no bloqueantes** construida para la gestión, geolocalización e inteligencia de flotas de transporte terrestre y activos en tránsito. 

La plataforma procesa flujos continuos de telemetría GPS a gran escala, computa dinámicamente distancias geodésicas ortodrómicas y tiempos estimados de arribo (**ETA**) con la fórmula de Haversine, supervisa límites operacionales mediante **geocercas circulares (Geofencing)**, y evalúa reglas de seguridad en tiempo real para disparar **alertas críticas** (exceso de velocidad, activación de botón de pánico, niveles críticos de batería y combustible) con difusión inmediata vía **Server-Sent Events (SSE)** y **WebSockets**.

---

## 🏛️ Arquitectura de Software

La arquitectura adopta el paradigma de **Sistemas Reactivos (Reactive Manifesto)** mediante el stack no bloqueante de **Spring WebFlux** sustentado en el motor de eventos de **Project Reactor** y el servidor de alto rendimiento **Netty**. 

### Principios Arquitectónicos
1. **Asincronía y No-Bloqueo de Extremo a Extremo**: Desde la capa de transporte HTTP y WebSockets hasta el acceso a datos reactivo (**R2DBC**), ningún hilo de ejecución se bloquea esperando I/O.
2. **Clean Layered Architecture**: Estricta separación de responsabilidades:
   - **Capa de Controladores (`controller`)**: Manejo de transporte, serialización y contratos HTTP REST.
   - **Capa de Servicios (`service` / `impl`)**: Lógica pura de negocio, algoritmos matemáticos y orquestación reactiva.
   - **Capa de Persistencia (`repository`)**: Repositorios reactivos `ReactiveCrudRepository`.
   - **Capa de Modelos y DTOs (`model`, `dto`)**: Inmutabilidad, validación con Bean Validation y desacoplamiento de la base de datos.
3. **Manejo Centralizado de Excepciones**: Uso de `@RestControllerAdvice` con respuestas homogéneas RFC-7807 y mapeo semántico de códigos HTTP.
4. **Resiliencia y Backpressure**: Los canales de streaming telemétrico gestionan saturación de eventos mediante `Sinks.Many` con estrategias `buffer` y `drop`.
5. **Autenticación Reactiva**: Seguridad perimetral con `Spring Security WebFlux` y cifrado BCrypt.

```mermaid
flowchart TD
    subgraph Ingress["Fuentes de Datos & Clientes"]
        GPS_DEV["📡 Unidades GPS / Dispositivos IoT"]
        OPERATOR["💻 Panel de Operador Logístico"]
        ADMIN["🛡️ Consola de Administrador"]
    end

    subgraph SecurityLayer["Capa de Seguridad Reactiva"]
        AUTH["🔐 Spring Security WebFlux (HTTP Basic + BCrypt)"]
    end

    subgraph CoreServices["Ecosistema de Microservicios Reactivos"]
        direction TB
        MS1["🚛 Fleet-Management-Service\n(Puerto 8081)\n• CRUD Flota, Choferes, Envíos y Usuarios\n• HATEOAS Hyperlinks (_links)"]
        MS2["📡 GPS-Ingestion-Service\n(Puerto 8082)\n• Ingesta Masiva / Batch\n• WebSockets Dúplex & SSE Stream"]
        MS3["🗺️ Route-Calculation-Service\n(Puerto 8083)\n• Algoritmo Haversine & ETA Dinámico\n• Motor de Geocercas (INSIDE / OUTSIDE)"]
        MS4["🚨 Alert-Service\n(Puerto 8084)\n• Detección de Anomalías Telemétricas\n• Ciclo de Vida de Alertas & Difusión SSE"]
    end

    subgraph DataStorage["Persistencia Reactiva Multibase"]
        H2[("💾 H2 In-Memory R2DBC\n(Evaluación Instantánea Sin Configuración)")]
        POSTGIS[("🐘 PostgreSQL 15 + PostGIS\n(Modo Producción / Supabase)")]
        REDIS[("⚡ Redis 7 Alpine\n(Pub/Sub & Caché)")]
    end

    GPS_DEV -->|REST POST / WebSockets| MS2
    OPERATOR -->|REST GET, PUT, POST / SSE| MS1
    OPERATOR -->|REST POST| MS3
    OPERATOR -->|REST & SSE Stream| MS4
    ADMIN -->|REST CRUD Usuarios| MS1

    AUTH -.-> MS1
    AUTH -.-> MS2
    AUTH -.-> MS3
    AUTH -.-> MS4

    MS1 -->|R2DBC| H2
    MS2 -->|R2DBC| H2
    MS3 -->|R2DBC| H2
    MS4 -->|R2DBC| H2

    MS1 -.->|Perfil postgres| POSTGIS
    MS2 -.->|Perfil postgres| POSTGIS
    MS3 -.->|Perfil postgres| POSTGIS
    MS4 -.->|Perfil postgres| POSTGIS
    MS2 -.->|Pub/Sub| REDIS
```

---

## 🧩 Diagrama de Componentes

El siguiente diagrama detalla la descomposición modular interna de cada microservicio, exhibiendo controladores, servicios, interfaces de repositorio, procesadores de streaming reactivo, configuración de seguridad y mecanismos de persistencia:

```mermaid
flowchart TD
    subgraph FleetService["📦 Fleet-Management-Service (Puerto 8081)"]
        FC1["UserController"]
        FC2["VehicleController"]
        FC3["DriverController"]
        FC4["ShipmentController"]
        
        FS1["UserServiceImpl"]
        FS2["VehicleServiceImpl"]
        FS3["DriverServiceImpl"]
        FS4["ShipmentServiceImpl"]
        
        FR1[("UserRepository")]
        FR2[("VehicleRepository")]
        FR3[("DriverRepository")]
        FR4[("ShipmentRepository")]
        
        FHateoas["HATEOAS Model Assembler"]
        FSec["SecurityConfig (Basic Auth + BCrypt)"]
        FExc["GlobalExceptionHandler"]
    end

    subgraph GPSService["📡 GPS-Ingestion-Service (Puerto 8082)"]
        GC1["TelemetryController"]
        GWS["TelemetryWebSocketHandler"]
        GS1["TelemetryServiceImpl"]
        GSink["Sinks.Many<TelemetryRecord>\n(Reactive Telemetry Hub)"]
        GR1[("TelemetryRepository")]
        GExc["GlobalExceptionHandler"]
    end

    subgraph RouteService["🗺️ Route-Calculation-Service (Puerto 8083)"]
        RC1["RouteCalculationController"]
        RC2["GeofenceController"]
        RS1["RouteCalculationServiceImpl"]
        RS2["GeofenceServiceImpl"]
        RMath["HaversineGeodesicEngine"]
        RR1[("RouteCalculationRepository")]
        RR2[("GeofenceRepository")]
        RExc["GlobalExceptionHandler"]
    end

    subgraph AlertService["🚨 Alert-Service (Puerto 8084)"]
        AC1["AlertController"]
        AS1["AlertServiceImpl"]
        ARule["RuleEvaluationEngine\n(Speed, Battery, Fuel, Panic)"]
        ASink["Sinks.Many<Alert>\n(Live Alert Stream)"]
        AR1[("AlertRepository")]
        AR2[("AlertRuleRepository")]
        AExc["GlobalExceptionHandler"]
    end

    FC1 --> FS1 --> FR1
    FC2 --> FS2 --> FR2
    FC3 --> FS3 --> FR3
    FC4 --> FS4 --> FR4
    FS2 -.-> FHateoas
    FS4 -.-> FHateoas

    GC1 --> GS1
    GWS --> GS1
    GS1 --> GR1
    GS1 --> GSink

    RC1 --> RS1 --> RR1
    RC2 --> RS2 --> RR2
    RS1 --> RMath
    RS2 --> RMath

    AC1 --> AS1
    AS1 --> ARule
    AS1 --> AR1
    AS1 --> AR2
    AS1 --> ASink
```

---

## 🏛️ Diagrama de Clases (Dominio del Sistema)

El modelo de dominio está estructurado para soportar las operaciones críticas del ciclo de vida logístico:

```mermaid
classDiagram
    direction TB

    class AppUser {
        +Long id
        +String username
        +String password
        +String email
        +String fullName
        +String role
        +LocalDateTime createdAt
    }

    class Vehicle {
        +Long id
        +String licensePlate
        +String brand
        +String model
        +Integer year
        +Double loadCapacityKg
        +String status
        +Long assignedDriverId
        +LocalDateTime createdAt
    }

    class Driver {
        +Long id
        +String fullName
        +String licenseNumber
        +String phoneNumber
        +String email
        +Boolean active
        +LocalDateTime createdAt
    }

    class Shipment {
        +Long id
        +String trackingNumber
        +String description
        +String originAddress
        +String destinationAddress
        +Double destLatitude
        +Double destLongitude
        +String status
        +Long vehicleId
        +LocalDateTime createdAt
    }

    class TelemetryRecord {
        +Long id
        +Long vehicleId
        +String deviceId
        +Double latitude
        +Double longitude
        +Double speedKmh
        +Double headingDegrees
        +Double batteryLevel
        +Double fuelLevel
        +LocalDateTime timestamp
    }

    class Geofence {
        +Long id
        +String name
        +Double latitude
        +Double longitude
        +Double radiusKm
        +Boolean active
        +String description
    }

    class RouteCalculationRecord {
        +Long id
        +Long vehicleId
        +Double originLatitude
        +Double originLongitude
        +Double destLatitude
        +Double destLongitude
        +Double distanceKm
        +Double speedKmh
        +Double estimatedHours
        +Double estimatedMinutes
        +LocalDateTime createdAt
    }

    class Alert {
        +Long id
        +Long vehicleId
        +String alertType
        +String severity
        +String description
        +Double latitude
        +Double longitude
        +String status
        +LocalDateTime resolvedAt
        +LocalDateTime createdAt
    }

    class AlertRule {
        +Long id
        +String ruleCode
        +String ruleName
        +String description
        +Double thresholdValue
        +Boolean active
    }

    Vehicle "1" --> "0..1" Driver : conduce
    Shipment "0..*" --> "1" Vehicle : transportado por
    TelemetryRecord "0..*" --> "1" Vehicle : emite telemetría
    RouteCalculationRecord "0..*" --> "1" Vehicle : asocia ruta a
    Alert "0..*" --> "1" Vehicle : reporta incidente en
    Alert "0..*" --> "1" AlertRule : evaluado contra
```

---

## 🚀 Diagrama de Despliegue

El despliegue está contenerizado para permitir ejecución aislada, reproducible y escalable:

```mermaid
flowchart LR
    subgraph External["Red Externa / Clientes"]
        DevGPS["📡 Dispositivos GPS"]
        WebBrowser["🌐 Navegador Web / Swagger UI"]
        PostmanClient["⚡ Cliente REST / cURL"]
    end

    subgraph Host["Host / Servidor de Despliegue (Docker Host)"]
        subgraph DockerNet["Red Virtual Docker (omnitrack-net)"]
            subgraph Cont1["Contenedor: omnitrack-fleet-service"]
                App1["Fleet Management Service\n(JVM OpenJDK 17 Netty)\nPuerto Interno: 8081"]
            end

            subgraph Cont2["Contenedor: omnitrack-gps-service"]
                App2["GPS Ingestion Service\n(JVM OpenJDK 17 Netty)\nPuerto Interno: 8082"]
            end

            subgraph Cont3["Contenedor: omnitrack-route-service"]
                App3["Route Calculation Service\n(JVM OpenJDK 17 Netty)\nPuerto Interno: 8083"]
            end

            subgraph Cont4["Contenedor: omnitrack-alert-service"]
                App4["Alert Service\n(JVM OpenJDK 17 Netty)\nPuerto Interno: 8084"]
            end

            subgraph DBCont["Contenedor: omnitrack-postgres"]
                PostgresDB[("PostgreSQL 15 + PostGIS 3.3\nPuerto Interno: 5432")]
            end

            subgraph CacheCont["Contenedor: omnitrack-redis"]
                RedisStore[("Redis 7 Alpine\nPuerto Interno: 6379")]
            end
        end

        Volume1[("📁 Volumen Docker: pgdata")]
        PostgresDB --- Volume1
    end

    WebBrowser -->|HTTP 8081| App1
    DevGPS -->|HTTP / WS 8082| App2
    WebBrowser -->|HTTP 8083| App3
    PostmanClient -->|HTTP / SSE 8084| App4

    App1 -->|R2DBC :5432| PostgresDB
    App2 -->|R2DBC :5432| PostgresDB
    App3 -->|R2DBC :5432| PostgresDB
    App4 -->|R2DBC :5432| PostgresDB
    App2 -->|Redis Protocol :6379| RedisStore
```

---

## 📊 Matriz de Endpoints de la API REST

### 1. `fleet-management-service` (Puerto 8081)
| Método | Endpoint | Rol Mínimo | Descripción |
|---|---|---|---|
| `POST` | `/api/v1/users` | `ROLE_ADMIN` | Registro de nuevo usuario (Cifra clave con BCrypt) |
| `GET` | `/api/v1/users` | `ROLE_ADMIN` | Listado general de usuarios |
| `GET` | `/api/v1/users/{id}` | `ROLE_ADMIN` | Consulta de usuario por ID |
| `GET` | `/api/v1/users/username/{username}` | `ROLE_ADMIN` | Búsqueda por nombre de usuario |
| `PUT` | `/api/v1/users/{id}` | `ROLE_ADMIN` | Actualización de datos de usuario |
| `DELETE` | `/api/v1/users/{id}` | `ROLE_ADMIN` | Eliminación de usuario |
| `GET` | `/api/v1/vehicles` | `ROLE_USER` | Consulta de flota (incluye `_links` HATEOAS) |
| `GET` | `/api/v1/vehicles/{id}` | `ROLE_USER` | Consulta de vehículo por ID con HATEOAS |
| `POST` | `/api/v1/vehicles` | `ROLE_USER` | Alta de nuevo vehículo con validación de placas |
| `PUT` | `/api/v1/vehicles/{id}` | `ROLE_USER` | Actualización de vehículo |
| `DELETE` | `/api/v1/vehicles/{id}` | `ROLE_ADMIN` | Baja de vehículo de la flota |
| `GET` | `/api/v1/drivers` | `ROLE_USER` | Listado de operadores de transporte |
| `GET` | `/api/v1/drivers/{id}` | `ROLE_USER` | Detalle de operador por ID |
| `POST` | `/api/v1/drivers` | `ROLE_USER` | Registro de operador con validación de licencia y email |
| `PUT` | `/api/v1/drivers/{id}` | `ROLE_USER` | Actualización de chofer |
| `DELETE` | `/api/v1/drivers/{id}` | `ROLE_ADMIN` | Eliminación de chofer |
| `GET` | `/api/v1/shipments` | `ROLE_USER` | Consulta de guías de envío |
| `GET` | `/api/v1/shipments/{id}` | `ROLE_USER` | Consulta de guía con HATEOAS |
| `POST` | `/api/v1/shipments` | `ROLE_USER` | Creación de despacho logístico |
| `PUT` | `/api/v1/shipments/{id}` | `ROLE_USER` | Actualización de envío |
| `DELETE` | `/api/v1/shipments/{id}` | `ROLE_ADMIN` | Cancelación/Eliminación de envío |

### 2. `gps-ingestion-service` (Puerto 8082)
| Método | Endpoint | Rol Mínimo | Descripción |
|---|---|---|---|
| `POST` | `/api/v1/telemetry` | `ROLE_USER` | Ingesta individual de telemetría GPS en tiempo real |
| `POST` | `/api/v1/telemetry/batch` | `ROLE_USER` | Ingesta por lote (batch) de telemetrías GPS |
| `GET` | `/api/v1/telemetry/latest/{vehicleId}` | `ROLE_USER` | Última posición conocida de un vehículo |
| `GET` | `/api/v1/telemetry/history/{vehicleId}` | `ROLE_USER` | Historial completo de coordenadas del vehículo |
| `GET` | `/api/v1/telemetry/stream` | `ROLE_USER` | **Server-Sent Events (SSE)** en vivo para monitores en tiempo real |
| `WS` | `/ws/telemetry` | Público / WS | Canal bidireccional por WebSockets para trackers IoT |

### 3. `route-calculation-service` (Puerto 8083)
| Método | Endpoint | Rol Mínimo | Descripción |
|---|---|---|---|
| `POST` | `/api/v1/routes/calculate-eta` | `ROLE_USER` | Cálculo geodésico de distancia Haversine y ETA en horas/minutos |
| `GET` | `/api/v1/routes/history/{vehicleId}` | `ROLE_USER` | Historial de estimaciones calculadas para un vehículo |
| `GET` | `/api/v1/geofences` | `ROLE_USER` | Listado de geocercas registradas |
| `GET` | `/api/v1/geofences/{id}` | `ROLE_USER` | Consulta de geocerca por ID |
| `POST` | `/api/v1/geofences` | `ROLE_USER` | Creación de perímetro geocercado circular |
| `PUT` | `/api/v1/geofences/{id}` | `ROLE_USER` | Modificación de geocerca |
| `DELETE` | `/api/v1/geofences/{id}` | `ROLE_ADMIN` | Eliminación de geocerca |
| `POST` | `/api/v1/geofences/check` | `ROLE_USER` | Evaluación de presencia: Determina si coordenadas están `INSIDE` o `OUTSIDE` |

### 4. `alert-service` (Puerto 8084)
| Método | Endpoint | Rol Mínimo | Descripción |
|---|---|---|---|
| `POST` | `/api/v1/alerts/evaluate-telemetry` | `ROLE_USER` | Motor de reglas: Evalúa telemetría y genera alertas automáticas |
| `GET` | `/api/v1/alerts` | `ROLE_USER` | Listado general de alertas del sistema |
| `GET` | `/api/v1/alerts/{id}` | `ROLE_USER` | Detalle de alerta específica |
| `GET` | `/api/v1/alerts/vehicle/{vehicleId}` | `ROLE_USER` | Filtro de incidentes por vehículo |
| `GET` | `/api/v1/alerts/status/{status}` | `ROLE_USER` | Filtro por estado (`ACTIVE` / `RESOLVED`) |
| `POST` | `/api/v1/alerts` | `ROLE_USER` | Creación manual de alerta |
| `PATCH` | `/api/v1/alerts/{id}/resolve` | `ROLE_USER` | Resolución de alerta con marca temporal |
| `GET` | `/api/v1/alerts/rules` | `ROLE_USER` | Catálogo de reglas y umbrales configurados |
| `GET` | `/api/v1/alerts/stream` | `ROLE_USER` | **Server-Sent Events (SSE)** en vivo para consola de despachadores |

---

## 🏆 Cumplimiento Riguroso de Criterios de Evaluación

| # | Criterio de Evaluación | Estado | Evidencia en el Código |
|---|---|:---:|---|
| **1** | **Correcta implementación de los endpoints REST** | ✅ Cumplido | Verbos HTTP semánticos (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`), cabeceras `Location` en `201 Created`, y respuestas estandarizadas. |
| **2** | **Conexión exitosa y funcional con la base de datos (H2)** | ✅ Cumplido | R2DBC H2 in-memory en los 4 servicios con scripts automáticos `schema.sql` y datos semilla precargados. |
| **3** | **Unit test para cada endpoint** | ✅ Cumplido | **46 pruebas unitarias implementadas** con `@WebFluxTest`, `WebTestClient` y Mockito. Cobertura del 100% de endpoints y flujos de error. |
| **4** | **Uso adecuado de patrones y principios de Spring Boot** | ✅ Cumplido | Inyección de dependencias por constructor, Clean Layered Architecture, DTOs desacoplados, Reactor Sinks y componentes reutilizables. |
| **5** | **Endpoints protegidos con Spring Security (Basic Auth)** | ✅ Cumplido | `SecurityWebFilterChain` reactivo con roles diferenciados (`ROLE_ADMIN`, `ROLE_USER`) y swagger/actuator públicos. |
| **6** | **Manejo correcto de excepciones y códigos de estado** | ✅ Cumplido | `@RestControllerAdvice` en cada servicio: 400 (Validaciones/Errores de campo), 401 (Sin credenciales), 403 (Rol insuficiente), 404 (No encontrado), 409 (Duplicado). |
| **7** | **Calidad del código (organización, nomenclatura y comentarios)** | ✅ Cumplido | Nomenclatura expresiva Java/Spring, Javadoc integral, explicaciones de fórmulas matemáticas y diseño modular. |
| **8** | **Documentación OpenAPI API y ejemplos de requests** | ✅ Cumplido | Swagger UI interactivo en cada puerto y catálogo exhaustivo de más de 30 peticiones en `requests.http`. |
| **9** | **Endpoints para monitoreo activos (Actuator)** | ✅ Cumplido | Spring Boot Actuator habilitado en todos los servicios (`/actuator/health`, `/actuator/info`, `/actuator/metrics`). |

---

## 🌟 Puntos Extra Implementados

### 1. Validación Estricta de Inputs
- Implementación de Bean Validation (`jakarta.validation.constraints`):
  - `@NotBlank`, `@Size(min, max)` en cadenas.
  - `@Email` para validación estricta de correo electrónico.
  - `@DecimalMin`, `@DecimalMax` para rangos de coordenadas válidas en la Tierra (Lat: -90° a 90°, Lon: -180° a 180°).
  - `@Min`, `@Max` para capacidades de carga, año del vehículo y velocidades.
  - `@Pattern` para formatos de placas y teléfonos.
- Las solicitudes inválidas retornan `HTTP 400 Bad Request` con un mapa estructurado de errores por campo (`fieldErrors`).

### 2. CRUD Completo de Usuario
- Módulo `AppUser` en `fleet-management-service`:
  - Registro de usuarios con hash seguro de contraseñas mediante `BCryptPasswordEncoder`.
  - Operaciones completas: Crear, Listar, Buscar por ID, Buscar por Username, Actualizar y Eliminar.
  - Protección de seguridad: La gestión y eliminación de usuarios requiere estrictamente `ROLE_ADMIN`.

### 3. Hipervínculos HATEOAS
- Integración de `spring-boot-starter-hateoas` con soporte reactivo.
- Los recursos exponen enlaces `_links` navegables:
  - Enlaces `self` y colecciones `collection`.
  - Navegación transversal entre microservicios (de un vehículo a su streaming de telemetría y a su historial de rutas; de un envío a su vehículo asignado; de una alerta a su endpoint de resolución).

### 4. Soporte Multibase: PostgreSQL / PostGIS / Supabase
- Compatibilidad nativa sin modificar código mediante perfiles de Spring Boot:
  - **Perfil `h2` (por defecto)**: Inicia de inmediato en memoria.
  - **Perfil `postgres`**: Conecta a PostgreSQL / PostGIS / Supabase mediante el driver reactivo `r2dbc-postgresql`.
- Se incluye `docker-compose.yml` con imagen oficial `postgis/postgis:15-3.3` y Redis 7 para despliegue productivo inmediato.

---

## 🚀 Guía de Despliegue y Ejecución

### Opción A: Ejecución Instantánea Local (Zero Setup)
Requisitos: Java 17+ y Maven instalados.

1. **Ejecutar las 46 pruebas unitarias:**
```bash
mvn clean test
```

2. **Compilar los paquetes ejecutables:**
```bash
mvn clean package -DskipTests
```

3. **Iniciar todos los microservicios con un solo clic:**
- **En Windows:** Haz doble clic o ejecuta en terminal:
  ```cmd
  run-all-services.bat
  ```
- **En Linux / macOS:** Ejecuta:
  ```bash
  chmod +x run-all-services.sh
  ./run-all-services.sh
  ```

4. **Para detener los microservicios:**
- **En Windows:** Ejecuta `stop-all-services.bat`
- **En Linux / macOS:** Ejecuta `./stop-all-services.sh`

---

### Opción B: Despliegue con Docker Compose (Producción con PostGIS y Redis)
Requisitos: Docker Desktop instalado.

Levanta la base de datos PostgreSQL con PostGIS, Redis y los 4 microservicios en contenedores aislados:
```bash
docker compose up --build
```
Para detener el stack:
```bash
docker compose down
```

---

### Opción C: Despliegue en la Nube (Render, Railway, Supabase)
Cada microservicio incluye su propio `Dockerfile` multi-stage optimizado para despliegues en plataformas de nube:
1. Conecta tu repositorio de GitHub a **Render** o **Railway**.
2. Despliega una base de datos PostgreSQL en **Supabase** o en el proveedor de tu preferencia.
3. Configura las siguientes variables de entorno en tus servicios:
   - `SPRING_PROFILES_ACTIVE`: `postgres`
   - `POSTGRES_HOST`: `<host_de_tu_base_de_datos>`
   - `POSTGRES_PORT`: `5432`
   - `POSTGRES_DB`: `<nombre_bd>`
   - `POSTGRES_USER`: `<usuario>`
   - `POSTGRES_PASSWORD`: `<contraseña>`

---

## 🔑 Credenciales de Acceso (HTTP Basic Auth)

| Usuario | Contraseña | Rol Asignado | Permisos y Alcance |
|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN`, `ROLE_USER` | Acceso irrestricto: CRUD completo de usuarios, bajas físicas (`DELETE`) y configuración. |
| `user` | `user123` | `ROLE_USER` | Operaciones cotidianas de lectura, ingesta telemétrica, cálculo de rutas y alertas. |

---

## 📖 Enlaces Swagger UI (Documentación Interactiva)

Una vez iniciados los microservicios, accede a la documentación interactiva OpenAPI desde tu navegador:

- **Fleet Management Service:** [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- **GPS Ingestion Service:** [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- **Route Calculation Service:** [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
- **Alert Service:** [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)

---

## 🧪 Pruebas Rápidas con cURL

### 1. Ingestar coordenada GPS (GPS Ingestion)
```bash
curl -X POST http://localhost:8082/api/v1/telemetry \
  -u user:user123 \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "deviceId": "GPS-UNIT-01",
    "latitude": 19.4326,
    "longitude": -99.1332,
    "speedKmh": 82.5,
    "headingDegrees": 180.0,
    "batteryLevel": 92.0,
    "fuelLevel": 78.5
  }'
```

### 2. Calcular Distancia y ETA (Route Calculation)
```bash
curl -X POST http://localhost:8083/api/v1/routes/calculate-eta \
  -u user:user123 \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "currentLatitude": 19.4326,
    "currentLongitude": -99.1332,
    "destLatitude": 20.6596,
    "destLongitude": -103.3496,
    "currentSpeedKmh": 85.0
  }'
```

### 3. Evaluar Telemetría y Disparar Alerta (Alert Service)
```bash
curl -X POST http://localhost:8084/api/v1/alerts/evaluate-telemetry \
  -u user:user123 \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "speedKmh": 118.5,
    "latitude": 19.4326,
    "longitude": -99.1332,
    "batteryLevel": 12.0,
    "fuelLevel": 8.0,
    "panicButtonPressed": false
  }'
```

---

## 👨‍💻 Autor & Contacto
- **Desarrollador:** Israel Mecillas (`IsraQuanDev`)
- **Repositorio:** [https://github.com/IsraQuanDev/OmniTrack-Logistics](https://github.com/IsraQuanDev/OmniTrack-Logistics)
- **Licencia:** MIT License
