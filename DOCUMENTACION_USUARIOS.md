# 📚 Documentación del Sistema de Usuarios

## 🏗️ Estructura del Proyecto

### Arquitectura de Capas (Layered Architecture)

El sistema sigue el patrón **MVC + Servicios** típico de Spring Boot:

```
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE PRESENTACIÓN                      │
│                    UserController.java                       │
│              (Endpoints REST - API HTTP)                     │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE NEGOCIO                          │
│                IUserService.java (Interface)                 │
│                  UserService.java (Implementación)           │
│              (Lógica de negocio y validaciones)              │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────────┐
│                   CAPA DE PERSISTENCIA                       │
│                   UserRepository.java                        │
│              (Acceso a base de datos - JPA)                  │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ↓
┌─────────────────────────────────────────────────────────────┐
│                    CAPA DE DATOS                             │
│                      User.java (Entity)                      │
│                     (Modelo de dominio)                      │
└─────────────────────────────────────────────────────────────┘

          COMPONENTES AUXILIARES:
          ├── UserMapper.java (Conversión Entity ↔ DTO)
          └── DTOs (Transferencia de datos)
```

---

## 📦 Componentes Implementados

### 1. **Modelo de Datos (Entity)**

#### `User.java` - Entidad principal
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // ID único
    
    private String name;                // Nombre
    private String lastName;            // Apellido
    private String username;            // Usuario único (para login)
    private String password;            // Contraseña
    private int pin;                    // PIN (para login rápido)
    
    @ManyToOne
    private Sucursal sucursal;          // Sucursal asignada
    
    @Enumerated(EnumType.STRING)
    private Role role;                  // Rol del usuario
    
    private Long deliveryId;            // ID de reparto (si es repartidor)
    private boolean active;             // Estado activo/inactivo
    
    private LocalDateTime createdAt;    // Fecha de creación
    private LocalDateTime updatedAt;    // Fecha de actualización
}
```

**Características:**
- Auditoría automática con `@PrePersist` y `@PreUpdate`
- Relación ManyToOne con `Sucursal`
- Campo `deliveryId` distingue usuarios de reparto

---

### 2. **DTOs (Data Transfer Objects)**

Los DTOs son objetos que se usan para transferir datos entre capas, sin exponer directamente las entidades.

#### `UserDto.java` - Representación del usuario para respuestas
```java
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String username;
    private int pin;
    private Long sucursalId;
    private String sucursalName;
    private Role role;
    private Long deliveryId;
    private boolean active;
}
```
**Uso:** Respuestas de consultas de usuarios (GET)

---

#### `RegisterDto.java` - Datos para crear/actualizar usuarios
```java
public class RegisterDto {
    private String name;
    private String lastName;
    private String username;
    private String password;
    private int pin;
    private Long sucursalId;
    private Role role;
    private Long deliveryId;  // Obligatorio si role = DELIVERY
}
```
**Uso:** Crear nuevos usuarios o actualizar existentes

---

#### `LoginDto.java` - Credenciales de login
```java
public class LoginDto {
    private String username;   // Para login tradicional
    private String password;   // Para login tradicional
    private Integer pin;       // Para login por PIN
}
```
**Uso:** Login dual - puede usar username/password O solo PIN

---

#### `LoginResponseDto.java` - Respuesta del login
```java
public class LoginResponseDto {
    private Long userId;
    private String name;
    private String lastName;
    private String username;
    private Role role;
    private Long sucursalId;
    private String sucursalName;
    private Long deliveryId;
    private String message;
    private boolean isDelivery;  // Flag automático si tiene deliveryId
}
```
**Uso:** Información del usuario autenticado

---

### 3. **Roles de Usuario (Enum)**

```java
public enum Role {
    ADMIN,       // Administrador del sistema
    USER,        // Usuario web
    CONTROLLER,  // Controlador/Supervisor
    PICKER,      // Picker/Preparador
    DELIVERY     // Repartidor (requiere deliveryId)
}
```

---

### 4. **Repositorio (Data Access Layer)**

#### `UserRepository.java`
```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPin(int pin);
    List<User> findByRole(Role role);
    List<User> findBySucursalId(Long sucursalId);
    List<User> findByDeliveryIdIsNotNull();
}
```

**Métodos Automáticos por JPA:**
- `findByUsername()` - Busca por username único
- `findByPin()` - Busca por PIN único
- `findByRole()` - Todos los usuarios de un rol específico
- `findBySucursalId()` - Usuarios de una sucursal
- `findByDeliveryIdIsNotNull()` - Solo repartidores

---

### 5. **Mapper (Conversión de datos)**

#### `UserMapper.java`
```java
@Component
public class UserMapper {
    // Entity → DTO
    public UserDto toDto(User user);
    
    // RegisterDTO → Entity
    public User toEntity(RegisterDto dto);
    
    // Entity → LoginResponseDTO
    public LoginResponseDto toLoginResponse(User user);
}
```

**Responsabilidad:** Convertir entre objetos de dominio (Entity) y objetos de transferencia (DTO)

---

### 6. **Servicios (Business Logic Layer)**

#### `IUserService.java` - Interfaz del servicio
Define el contrato de operaciones disponibles:
```java
public interface IUserService {
    UserDto registerUser(RegisterDto registerDto);
    LoginResponseDto login(LoginDto loginDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    List<UserDto> getUsersByRole(Role role);
    List<UserDto> getUsersBySucursal(Long sucursalId);
    List<UserDto> getDeliveryUsers();
    UserDto updateUser(Long id, RegisterDto registerDto);
    void deleteUser(Long id);
    UserDto activateUser(Long id);
    UserDto deactivateUser(Long id);
}
```

---

#### `UserService.java` - Implementación

**Lógica de Negocio Principal:**

##### 1. **Registro de Usuarios**
```java
@Transactional
public UserDto registerUser(RegisterDto registerDto) {
    // Validaciones:
    // 1. Username único
    // 2. Sucursal válida
    // 3. Si role=DELIVERY → deliveryId obligatorio
    // 4. Si deliveryId presente → role debe ser DELIVERY
}
```

##### 2. **Login Dual**
```java
@Transactional(readOnly = true)
public LoginResponseDto login(LoginDto loginDto) {
    // Dos formas de login:
    // A. Por PIN (para dispositivos touch/kioscos)
    // B. Por username + password (para web)
    
    // Validaciones:
    // 1. Credenciales correctas
    // 2. Usuario activo
}
```

##### 3. **Actualización**
```java
@Transactional
public UserDto updateUser(Long id, RegisterDto registerDto) {
    // Actualiza todos los campos
    // Valida username único si cambió
    // Valida sucursal si cambió
}
```

---

### 7. **Controlador (REST API)**

#### `UserController.java`

**Endpoints Disponibles:**

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/users/register` | Registrar nuevo usuario |
| POST | `/api/users/login` | Login (username/password o PIN) |
| GET | `/api/users` | Obtener todos los usuarios |
| GET | `/api/users/{id}` | Obtener usuario por ID |
| GET | `/api/users/role/{role}` | Usuarios por rol |
| GET | `/api/users/sucursal/{sucursalId}` | Usuarios por sucursal |
| GET | `/api/users/delivery` | Solo repartidores |
| PUT | `/api/users/{id}` | Actualizar usuario |
| DELETE | `/api/users/{id}` | Eliminar usuario |
| PATCH | `/api/users/{id}/activate` | Activar usuario |
| PATCH | `/api/users/{id}/deactivate` | Desactivar usuario |

---

## 🔄 Flujos de Trabajo

### Flujo 1: Registro de Usuario Web

```
Usuario Web (sin deliveryId)
────────────────────────────

1. Cliente → POST /api/users/register
   {
     "name": "María",
     "lastName": "López",
     "username": "mlopez",
     "password": "pass123",
     "pin": 5678,
     "sucursalId": 1,
     "role": "USER",
     "deliveryId": null
   }

2. UserController recibe el request
   ↓
3. UserService valida:
   ✓ Username no existe
   ✓ Sucursal existe
   ✓ deliveryId es null (no es reparto)
   ↓
4. UserMapper convierte RegisterDto → User
   ↓
5. UserRepository guarda en BD
   ↓
6. UserMapper convierte User → UserDto
   ↓
7. Response HTTP 201 Created con UserDto
```

---

### Flujo 2: Registro de Repartidor

```
Usuario Repartidor (con deliveryId)
───────────────────────────────────

1. Cliente → POST /api/users/register
   {
     "name": "Carlos",
     "lastName": "Gómez",
     "username": "cgomez",
     "password": "pass456",
     "pin": 9012,
     "sucursalId": 1,
     "role": "DELIVERY",
     "deliveryId": 100  ← REQUERIDO
   }

2. UserService valida:
   ✓ deliveryId presente → role = DELIVERY ✓
   ✓ role = DELIVERY → deliveryId requerido ✓
   ↓
3. Guarda con ambos campos correctamente
```

---

### Flujo 3: Login por Username/Password

```
Login Tradicional
─────────────────

1. Cliente → POST /api/users/login
   {
     "username": "mlopez",
     "password": "pass123"
   }

2. UserService:
   ↓
3. Busca usuario por username
   ↓
4. Verifica password coincide
   ↓
5. Verifica usuario activo
   ↓
6. LoginResponseDto con datos completos:
   {
     "userId": 1,
     "name": "María",
     "lastName": "López",
     "username": "mlopez",
     "role": "USER",
     "sucursalId": 1,
     "sucursalName": "Sucursal Centro",
     "deliveryId": null,
     "isDelivery": false,
     "message": "Login exitoso"
   }
```

---

### Flujo 4: Login por PIN

```
Login Rápido (PIN para dispositivos)
────────────────────────────────────

1. Cliente → POST /api/users/login
   {
     "pin": 9012
   }

2. UserService:
   ↓
3. Busca usuario por PIN
   ↓
4. Verifica usuario activo
   ↓
5. LoginResponseDto:
   {
     "userId": 2,
     "name": "Carlos",
     "lastName": "Gómez",
     "role": "DELIVERY",
     "deliveryId": 100,
     "isDelivery": true,  ← Flag automático
     "message": "Login exitoso"
   }
```

---

## 🎯 Reglas de Negocio Implementadas

### 1. **Identificación de Repartidores**
```java
// Si tiene deliveryId → Es repartidor
if (user.getDeliveryId() != null) {
    // Automáticamente role = DELIVERY
    loginResponse.setIsDelivery(true);
}
```

### 2. **Validación de Coherencia**
```java
// DELIVERY debe tener deliveryId
if (role == DELIVERY && deliveryId == null) {
    throw new IllegalArgumentException("Los usuarios DELIVERY necesitan deliveryId");
}

// Si tiene deliveryId → debe ser DELIVERY
if (deliveryId != null && role != DELIVERY) {
    registerDto.setRole(Role.DELIVERY);  // Auto-corrección
}
```

### 3. **Username Único**
```java
if (userRepository.findByUsername(username).isPresent()) {
    throw new IllegalArgumentException("Username ya existe");
}
```

### 4. **Usuarios Activos/Inactivos**
```java
// Solo usuarios activos pueden hacer login
if (!user.isActive()) {
    throw new IllegalArgumentException("Usuario inactivo");
}

// Desactivar en lugar de eliminar (soft delete)
deactivateUser(id);  // active = false
```

---

## 🔐 Seguridad y Validaciones

### Nivel de Controlador (UserController)
- ✅ Validación de formato JSON
- ✅ CORS permitido (`@CrossOrigin`)
- ✅ Códigos HTTP apropiados (201, 200, 204, 404)

### Nivel de Servicio (UserService)
- ✅ Validación de usuario único
- ✅ Validación de existencia de recursos (Sucursal, User)
- ✅ Validación de reglas de negocio (DELIVERY ↔ deliveryId)
- ✅ Validación de usuario activo en login
- ✅ Transacciones (`@Transactional`)

### Nivel de Repositorio
- ✅ Constraints de BD (unique, nullable)
- ✅ Relaciones de integridad referencial

---

## 📊 Casos de Uso por Rol

### ADMIN
- Crear/editar/eliminar cualquier usuario
- Ver todos los usuarios
- Activar/desactivar usuarios

### CONTROLLER (Controlador/Supervisor)
- Login por PIN o username
- Supervisar operaciones
- Ver usuarios de su sucursal

### PICKER (Preparador)
- Login por PIN rápido
- Registrar productos preparados

### DELIVERY (Repartidor)
- Login por PIN rápido
- Tiene `deliveryId` para tracking
- `isDelivery = true` en respuesta

### USER (Usuario Web)
- Login tradicional username/password
- Sin deliveryId
- Acceso web completo

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología |
|------------|-----------|
| Framework | Spring Boot |
| Persistencia | Spring Data JPA |
| Base de Datos | JPA/Hibernate |
| Validaciones | Jakarta Validation |
| Mapeo | Lombok + Custom Mapper |
| API REST | Spring Web MVC |

---

## 📝 Ejemplos de Uso

### Crear Usuario de Reparto
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan",
    "lastName": "Pérez",
    "username": "jperez",
    "password": "mipass",
    "pin": 1234,
    "sucursalId": 1,
    "role": "DELIVERY",
    "deliveryId": 500
  }'
```

### Login por PIN
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"pin": 1234}'
```

### Obtener Todos los Repartidores
```bash
curl -X GET http://localhost:8080/api/users/delivery
```

### Desactivar Usuario
```bash
curl -X PATCH http://localhost:8080/api/users/5/deactivate
```

---

## 🧪 Cobertura Funcional

| Funcionalidad | Estado |
|---------------|--------|
| Registro de usuarios | ✅ |
| Login dual (PIN/Password) | ✅ |
| Diferenciación reparto/web | ✅ |
| CRUD completo | ✅ |
| Filtros por rol/sucursal | ✅ |
| Activación/Desactivación | ✅ |
| Validaciones de negocio | ✅ |
| Manejo de excepciones | ✅ |
| Auditoría (timestamps) | ✅ |

---

## 🎓 Conceptos Java/Spring Aplicados

### 1. **Inyección de Dependencias**
```java
@Autowired
private UserRepository userRepository;  // Spring inyecta la implementación
```

### 2. **Anotaciones JPA**
```java
@Entity           // Marca como entidad de BD
@Table            // Nombre de tabla
@Id               // Clave primaria
@GeneratedValue   // Auto-incremento
@ManyToOne        // Relación N:1
@Enumerated       // Enum como String en BD
```

### 3. **Transacciones**
```java
@Transactional              // Para escritura
@Transactional(readOnly=true)  // Para lectura (optimización)
```

### 4. **REST Controllers**
```java
@RestController   // Controlador REST
@RequestMapping   // Path base
@PostMapping      // POST endpoint
@GetMapping       // GET endpoint
@PathVariable     // Parámetro de URL
@RequestBody      // JSON en body
```

### 5. **Manejo de Excepciones**
```java
throw new ResourceNotFoundException("Usuario no encontrado");
throw new IllegalArgumentException("Validación fallida");
```

### 6. **Programación Funcional (Streams)**
```java
return users.stream()
    .map(userMapper::toDto)
    .collect(Collectors.toList());
```

---

## 📌 Notas Importantes

1. **Sin Seguridad Actual:** No hay JWT, BCrypt, Spring Security implementado
2. **Passwords en Texto Plano:** ⚠️ Agregar encriptación en producción
3. **CORS Abierto:** `@CrossOrigin(origins = "*")` - Restringir en producción
4. **Soft Delete:** Usar `deactivate` en lugar de `delete` para preservar datos

---

## 🚀 Próximos Pasos Sugeridos

- [ ] Implementar Spring Security
- [ ] Agregar BCrypt para passwords
- [ ] Implementar JWT para autenticación
- [ ] Agregar validaciones con `@Valid`
- [ ] Testing unitario e integración
- [ ] Documentación con Swagger/OpenAPI
- [ ] Implementar refresh tokens
- [ ] Agregar logs con SLF4J
- [ ] Rate limiting para login
- [ ] Historial de accesos

---

**Fecha de Documentación:** Abril 2026  
**Versión:** 1.0  
**Sistema:** Loop - Gestión de Usuarios
