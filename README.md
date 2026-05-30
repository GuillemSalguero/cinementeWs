# CineMente — User Backend

> Part of the CineMente Final Degree Project · Universitat de Girona · June 2026  
> Author: **Guillem Salguero Montes**

Spring Boot REST API that handles user management, authentication, and social features for the CineMente movie recommendation system. Acts as the main orchestrator: validates user context and delegates recommendation requests to the FastAPI AI engine.

---

## Table of Contents

- [Tech stack](#tech-stack)
- [Getting started](#getting-started)
- [API endpoints](#api-endpoints)
- [Project structure](#project-structure)
- [Key implementation details](#key-implementation-details)
- [Related repositories](#related-repositories)

---

## Tech stack

| | |
|--|--|
| Framework | Spring Boot 3.2.4 (Java 21) |
| ORM | JPA / Hibernate |
| Security | Spring Security · JWT (HMAC-SHA256) · BCrypt |
| Database | PostgreSQL via Supabase |
| Build | Maven |

---

## Getting started

### Prerequisites

- Java 21
- Maven
- A Supabase project with the CineMente schema applied

### Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres
    username: postgres
    password: YOUR_SUPABASE_PASSWORD

jwt:
  secret: YOUR_SECRET_MIN_32_CHARS   # generate with: openssl rand -hex 32
  expiration: 86400000               # access token (ms)
  refresh-expiration: 604800000      # refresh token (ms)
```

### Run

```bash
mvn spring-boot:run
```

Service starts at `http://localhost:8080`.

---

## API endpoints

### Auth

| Method | Endpoint | Description | Auth required |
|--------|----------|-------------|:---:|
| `POST` | `/api/auth/register` | Register a new user | No |
| `POST` | `/api/auth/login` | Login, returns JWT | No |
| `GET` | `/api/auth/me` | Get current user profile | Yes |

**Register / Login response:**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci...",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "name": "guillem"
  }
}
```

### Movies & recommendations

| Method | Endpoint | Description | Auth required |
|--------|----------|-------------|:---:|
| `POST` | `/api/movies/recommend` | Natural language recommendation (proxied to FastAPI) | Yes |
| `GET` | `/api/movies/search` | Classic search by title / genre / director | No |
| `GET` | `/api/movies/detail` | Full movie detail | No |

### User lists

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET/POST/DELETE` | `/api/user/favorites` | Manage favourite movies |
| `GET/POST/DELETE` | `/api/user/watchlist` | Manage watchlist |
| `GET/POST/DELETE` | `/api/user/directors` | Manage favourite directors |
| `GET/POST/DELETE` | `/api/user/reviews` | Manage personal reviews (1–5 rating) |

### Social

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET/POST/DELETE` | `/api/user/friends` | Manage friend relationships |
| `GET` | `/api/user/friends/{id}/watchlist` | View a friend's watchlist |
| `GET` | `/api/user/friends/{id}/favorites` | View a friend's favourites |

---

## Project structure

```
src/main/java/com/app/auth/
├── AuthApplication.java
├── config/
│   ├── SecurityConfig.java           # JWT filter chain, CORS, STATELESS sessions
│   └── GlobalExceptionHandler.java   # Maps exceptions to HTTP responses
├── controller/
│   └── AuthController.java
├── dto/
│   └── AuthDtos.java                 # Request/Response DTOs (separated from JPA entities)
├── entity/
│   └── User.java                     # JPA entity — never exposed directly to the API
├── repository/
│   └── UserRepository.java
├── security/
│   ├── JwtService.java               # Token generation and validation (HMAC-SHA256)
│   └── JwtAuthFilter.java            # Intercepts Bearer tokens on every request
└── service/
    ├── AuthService.java              # Register and login logic
    ├── MovieService.java             # Proxy to FastAPI + user context enrichment
    ├── DirectorService.java          # Favourite directors management
    └── UserDetailsServiceImpl.java
```

---

## Key implementation details

**Stateless authentication** — No server-side sessions. Every request is authenticated via a JWT signed with HMAC-SHA256. Two token types: short-lived access token and long-lived refresh token. Passwords stored exclusively as BCrypt hashes.

**Entity / DTO separation** — The JPA `User` entity is never serialised directly to the API. `AuthDtos.java` defines all request and response shapes, preventing sensitive fields from leaking to the client.

**Orchestration** — When the frontend requests a recommendation, Spring Boot validates the user context (watchlist, favourite directors) and forwards the enriched request to the FastAPI service via an internal HTTP call. This keeps the AI engine stateless and user-agnostic.

**Cascade deletes** — Friend relationships use `ON DELETE CASCADE`, so removing a user automatically cleans up all associated social data.

**Supabase trigger** — The database has a `validate_friends` trigger on the users table. Make sure the trigger function allows `friend = NULL` for users registering without a friend, otherwise registration will fail.

---

## Related repositories

| Service | Repository |
|---------|-----------|
| Frontend (React) | [cinemente-recommender](https://github.com/GuillemSalguero/cinemente-recommender) |
| AI engine (FastAPI) | [Recomenador](https://github.com/GuillemSalguero/Recomenador) |
| Evaluation suite | [Testing-Recomenador](https://github.com/GuillemSalguero/Testing-Recomenador) |

---

*Academic project — Universitat de Girona, June 2026.*
