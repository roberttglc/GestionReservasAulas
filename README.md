# Gestión de reservas de aulas

Aplicación REST desarrollada con Java 17, Spring Boot, Spring Data JPA, MySQL y Lombok. Gestiona aulas, profesores, franjas horarias y reservas mediante CRUD en `/aulas`, `/profesores`, `/franjas` y `/reservas`. Las altas devuelven `201`, las eliminaciones `204`, los recursos inexistentes `404` y los datos inválidos `400`.

## Reservas y consultas

Una reserva incluye `fechaReserva`, `fechaRealizacion` (se rellena automáticamente con `@PrePersist`) y referencias a aula, profesor y franja. No se permite reservar una fecha pasada ni solapar en la misma aula dos franjas para la misma fecha; estas reglas devuelven `409 Conflict`. Las aulas pueden filtrarse con `GET /aulas?ordenadores=true` y `GET /aulas?capacidad=20` (ambos filtros pueden combinarse).

## Puesta en marcha

Configura MySQL con las variables `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` (por defecto se usa `reservas_aulas` en localhost) y ejecuta `./mvnw spring-boot:run`. El frontend está en `frontend/`; puede abrirse directamente para usar la API local o servirse con cualquier servidor estático.

## Desarrollo asistido por IA

Se utilizó el prompt: “Implementa un backend Spring Boot con CRUD para aulas, profesores, reservas y franjas, validando fechas pasadas y franjas ocupadas, y añade un frontend sin frameworks”. La propuesta inicial no distinguía correctamente entre `400` (entrada inválida) y `409` (conflicto de reserva), por lo que se separaron ambas respuestas en un manejador global. También se revisó el filtrado combinado de aulas para que no dependiera de una consulta SQL duplicada.
