# 🧩 Usuarios Service

Microservicio encargado de la **gestión de usuarios** dentro del ecosistema AresFitness.  
Implementa registro, autenticación, roles y búsquedas avanzadas con paginación.

---

## 🧱 Arquitectura general

El servicio está construido en **Spring Boot 3**, con seguridad **JWT**, persistencia JPA y manejo de errores global uniforme.  
Cada usuario hereda de la entidad base `Usuario` usando la estrategia `JOINED`.

*usuarios-service/
├── Controller/
├── Service/
├── Repository/
├── Model/
├── DTO/
├── Config/
├── Exception/
└── UsuariosServiceApplication.java*


---

## ⚙️ Tecnologías principales

| Componente | Tecnología |
|-------------|-------------|
| Backend | Spring Boot 3.2 |
| Seguridad | Spring Security + JWT |
| Base de datos | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Validación | Jakarta Validation |
| Manejo de errores | GlobalExceptionHandler + ApiResponse |
| Logging | SLF4J / Logback |

---

## 🚀 Funcionalidades principales

### 🔐 Autenticación y Roles
- Registro de **clientes**, **recepcionistas** y **administradores**
- Autenticación mediante **JWT (stateless)**
- Roles: `ADMIN`, `RECEPCIONISTA`, `CLIENTE`
- Control de endpoints mediante anotaciones de seguridad

### 🔎 Búsquedas avanzadas
- Búsqueda por **nombre o apellido** (`/api/usuarios/search`)
- Filtros por **rol**, **estado activo** y **departamento**
- Soporte de **paginación y orden dinámico**

### 🧩 Manejo de errores uniforme
- Respuestas estandarizadas (`ApiResponse<T>`)
- Manejador global (`GlobalExceptionHandler`)
- Excepciones personalizadas (`BusinessException`, `EntityNotFoundException`)

---

## 📡 Endpoints principales

| Método | Endpoint | Descripción |
|--------|-----------|--------------|
| `POST` | `/api/usuarios/login` | Inicia sesión y devuelve un JWT |
| `POST` | `/api/usuarios/clientes` | Registra un nuevo cliente |
| `POST` | `/api/usuarios/recepcionistas` | Registra un recepcionista |
| `POST` | `/api/usuarios/administradores` | Registra un administrador |
| `GET` | `/api/usuarios/search?nombre=` | Busca por nombre o apellido |
| `GET` | `/api/usuarios/search/paginado` | Búsqueda paginada |
| `GET` | `/api/usuarios/search/activos` | Lista solo usuarios activos |
| `GET` | `/api/usuarios/search/rol?rol=` | Filtra por rol |
| `GET` | `/api/usuarios/search/departamento` | Filtra por departamento |

---

## 🧠 Ejemplo de respuesta estándar
---
```json
{
  "timestamp": "2025-10-28T11:35:00",
  "statusCode": 200,
  "message": "Usuarios encontrados",
  "data": [
    {
      "id": 1,
      "nombre": "Alexandro",
      "apellido": "Gallardo",
      "email": "alex@gmail.com",
      "role": "ADMIN"
    }
  ]
}
```
## 🧰 Variables de entorno

| Variable                     | Descripción                      | Ejemplo                                        |
| ---------------------------- | -------------------------------- | ---------------------------------------------- |
| `SPRING_DATASOURCE_URL`      | URL de la base de datos          | `jdbc:postgresql://localhost:5432/usuarios_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de DB                    | `postgres`                                     |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de DB                 | `Ale17`                                        |
| `JWT_SECRET`                 | Clave secreta para firmar tokens | `aresfitness-secret-key`                       |
| `SERVER_PORT`                | Puerto del servicio              | `8081`                                         |

## 🐳 Ejecución con Docker

docker build -t usuarios-service .
docker run -p 8081:8081 usuarios-service

## con docker-compose:
usuarios-service:
build: ./usuarios-service
ports:
- "8081:8081"
environment:
- SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/usuarios_db
- SPRING_DATASOURCE_USERNAME=postgres
- SPRING_DATASOURCE_PASSWORD=Ale17
- JWT_SECRET=aresfitness-secret-key

