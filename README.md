# Loop - Sistema de Control Logístico

## 📋 Descripción

**Loop** es una aplicación de gestión logística diseñada para el control integral de mercadería en operaciones de entrada y salida. La plataforma permite administrar el flujo de productos, gestionar inventarios y coordinar pedidos internos para carga de vehículos de distribución.

## 🎯 Funcionalidades Principales

### Control de Mercadería
- **Entrada de Productos**: Registro y seguimiento de mercadería que ingresa al almacén
- **Salida de Productos**: Control de productos que salen de las instalaciones
- **Trazabilidad Completa**: Seguimiento detallado de movimientos de inventario por sucursal

### Gestión de Pedidos Internos
- **Creación de Órdenes**: Los usuarios con rol DELIVERY pueden generar pedidos internos
- **Gestión de Descartables**: Control de materiales desechables necesarios para la operación
- **Estados de Pedido**: Seguimiento de órdenes desde PENDIENTE hasta COMPLETADO
- **Filtros por Fecha**: Consulta de pedidos del día con filtrado por estado

### Administración
- **Gestión de Usuarios**: Control de acceso con sistema de roles (Admin, Usuario, Controlador, Picker, Delivery)
- **Gestión de Productos**: Catálogo completo de productos con códigos y precios
- **Gestión de Sucursales**: Administración de múltiples ubicaciones
- **Reportes de Control**: Visualización de movimientos y estados de inventario

## 🏗️ Arquitectura

### Stack Tecnológico
- **Backend**: Spring Boot 4.0.5
- **Java**: JDK 17
- **Persistencia**: JPA/Hibernate
- **Base de Datos**: PostgreSQL / SQL Server / H2
- **Validación**: Jakarta Bean Validation
- **Seguridad**: Spring Security Crypto (para encriptación de contraseñas)

### Patrón de Diseño
La aplicación sigue una arquitectura en capas:
- **Controller**: Endpoints REST para la API
- **Service**: Lógica de negocio con transacciones
- **Repository**: Acceso a datos con Spring Data JPA
- **DTO**: Objetos de transferencia de datos con validaciones
- **Mapper**: Conversión entre entidades y DTOs
- **Model**: Entidades JPA del dominio

## 🚀 Inicio Rápido

### Requisitos Previos
- JDK 17 o superior
- Maven 3.6+
- PostgreSQL / SQL Server (o H2 para desarrollo)

### Configuración

1. Clonar el repositorio
2. Configurar la base de datos en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/loop_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

3. Compilar y ejecutar:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## 📦 Módulos del Sistema

### Usuarios
Gestión de personal con roles específicos:
- **ADMIN**: Acceso completo al sistema
- **USER**: Usuario estándar
- **CONTROLLER**: Control de inventario
- **PICKER**: Preparación de pedidos
- **DELIVERY**: Gestión de entregas y pedidos internos

### Productos
Catálogo de mercadería con:
- Código único
- Descripción
- Precio
- Control de stock

### Sucursales
Gestión de múltiples ubicaciones con:
- Nombre
- Dirección
- Información de contacto

### Controles
Registro de movimientos de inventario:
- Entrada/salida de productos
- Cantidades
- Fecha y hora
- Responsable del movimiento

### Órdenes (Pedidos Internos)
Sistema de pedidos para carga de vehículos:
- Asignación a delivery
- Sucursal de destino
- Items descartables
- Estados: PENDIENTE / COMPLETADO
- Total calculado automáticamente

## 🛡️ Seguridad

- Encriptación de contraseñas con BCrypt
- Validación de datos de entrada con Jakarta Validation
- Gestión de errores centralizada con excepciones personalizadas

## 📊 Modelo de Datos

El sistema gestiona las siguientes entidades principales:
- **User**: Usuarios del sistema
- **Product**: Catálogo de productos
- **Sucursal**: Ubicaciones/sucursales
- **Control**: Registros de entrada/salida
- **Order**: Pedidos internos
- **OrderItem**: Items de pedidos
- **Disposable**: Materiales descartables

## 🔄 Estado del Proyecto

### Implementado ✅
- CRUD completo de todas las entidades
- Sistema de órdenes con estado
- Filtrado de pedidos por fecha y estado
- Validaciones de datos
- Gestión transaccional
- Arquitectura escalable

### Próximas Mejoras 🚧
- Autenticación y autorización con Spring Security
- Generación de reportes PDF/Excel
- Dashboard de métricas en tiempo real
- Notificaciones de estado de pedidos

## 👥 Contribución

Para contribuir al proyecto:
1. Crear una rama feature desde `main`
2. Realizar los cambios necesarios
3. Asegurar que todas las pruebas pasen
4. Crear un Pull Request

## 📝 Licencia

Este proyecto es privado y de uso interno.

---

**Loop** - Simplificando la logística, un pedido a la vez.
