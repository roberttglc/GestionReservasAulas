# AGENTS.md

## Contexto
Este proyecto es una API REST para gestionar aulas, profesores, franjas horarias y reservas.

## Reglas de desarrollo
- Usar Java, Spring Boot, Spring Data JPA y Lombok.
- Mantener los endpoints REST en español (`/aulas`, `/profesores`, `/franjas`, `/reservas`).
- Validar las reservas antes de persistirlas: no permitir fechas pasadas ni solapamiento de aula y franja.
- Devolver errores JSON claros y códigos HTTP semánticos.
- El frontend debe permanecer en `frontend/` y usar únicamente HTML, CSS y JavaScript.
- No guardar credenciales reales en el repositorio; configurar MySQL mediante variables `DB_URL`, `DB_USERNAME` y `DB_PASSWORD`.
