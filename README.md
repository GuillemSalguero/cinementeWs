# Spring Boot + Supabase Auth API

REST API con registro e inicio de sesión JWT conectado a tu base de datos Supabase.

---

## Configuración rápida

### 1. Configura `application.yml`

Abre `src/main/resources/application.yml` y reemplaza:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.TU_PROJECT_REF.supabase.co:5432/postgres
    password: TU_PASSWORD_DE_SUPABASE

jwt:
  secret: CAMBIA_ESTO_POR_UN_SECRET_LARGO_AL_MENOS_32_CHARS
```

**Cómo obtener tus credenciales Supabase:**
- Project Ref: `https://app.supabase.com` → Settings → General
- DB Password: Settings → Database → Connection string

**Generar un JWT secret seguro:**
```bash
openssl rand -hex 32
```

---

## ⚠️ Limitación: columna `pasword bigint`

Tu tabla tiene `pasword bigint`, pero BCrypt produce strings como:
`$2a$10$...` que no caben en un entero.

**Solución recomendada — migrar a TEXT en Supabase:**

```sql
-- Ejecuta esto en el SQL Editor de Supabase
ALTER TABLE public."Users" ALTER COLUMN pasword TYPE text USING pasword::text;
```

Si haces esto, actualiza `User.java` cambiando:
```java
// Antes
private Long pasword;

// Después
private String pasword;
```

Y en `AuthService.java` simplifica a BCryptPasswordEncoder estándar.

> La versión actual funciona con `bigint` usando hashCode() como workaround, pero es menos seguro.

---

## Arrancar el proyecto

```bash
mvn spring-boot:run
```

---

## Endpoints

### Registro
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "juan",
  "password": "miPassword123",
  "favFilms": "El Padrino, Matrix",
  "friend": null
}
```

**Respuesta 201:**
```json
{
  "accessToken": "eyJhbGci...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "name": "juan",
    "favFilms": "El Padrino, Matrix",
    "friend": null
  }
}
```

---

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "name": "juan",
  "password": "miPassword123"
}
```

**Respuesta 200:** (mismo formato que register)

---

### Endpoint protegido (ejemplo)
```http
GET /api/auth/me
Authorization: Bearer eyJhbGci...
```

---

## Estructura del proyecto

```
src/main/java/com/app/auth/
├── AuthApplication.java
├── config/
│   ├── SecurityConfig.java       ← Spring Security + JWT filter chain
│   └── GlobalExceptionHandler.java
├── controller/
│   └── AuthController.java       ← /api/auth/register, /login, /me
├── dto/
│   └── AuthDtos.java             ← Request/Response DTOs
├── entity/
│   └── User.java                 ← Mapeado a public."Users"
├── repository/
│   └── UserRepository.java
├── security/
│   ├── JwtService.java           ← Genera y valida tokens JWT
│   └── JwtAuthFilter.java        ← Intercepta peticiones con Bearer token
└── service/
    ├── AuthService.java           ← Lógica de registro y login
    └── UserDetailsServiceImpl.java
```

---

## Trigger `check_friends`

Tu tabla tiene un trigger `validate_friends` que se ejecuta en INSERT/UPDATE.
Asegúrate de que la función `validate_friends` en Supabase permita `friend = NULL`
para usuarios que se registran sin amigo, o el registro fallará.

```sql
-- Verificar la función del trigger en Supabase
SELECT prosrc FROM pg_proc WHERE proname = 'validate_friends';
```
