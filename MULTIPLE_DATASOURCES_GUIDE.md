# Configuración de Múltiples Bases de Datos (PostgreSQL + SQL Server)

## 📋 Descripción
Configuración de múltiples DataSources para permitir que algunos repositorios se conecten a PostgreSQL y otros a SQL Server.

## 🏗️ Arquitectura

```
src/main/java/com/eljumillano/loop/
├── config/
│   ├── PostgresDataSourceConfig.java    # Configuración PostgreSQL (Primary)
│   └── SqlServerDataSourceConfig.java   # Configuración SQL Server (Secondary)
├── repository/
│   ├── postgres/                        # Repositorios que usan PostgreSQL
│   │   ├── UserRepository.java
│   │   ├── ProductRepository.java
│   │   └── ...
│   └── sqlserver/                       # Repositorios que usan SQL Server
│       ├── ExternalDataRepository.java
│       └── ...
└── model/
    ├── User.java                        # Entidades compartidas
    ├── Product.java
    └── ...
```

## 🚀 Pasos para Implementar

### 1️⃣ Reorganizar Repositorios

Debes mover tus repositorios a los paquetes correspondientes según la base de datos:

#### Opción A: Mover repositorios manualmente

**Para PostgreSQL:**
```
repository/UserRepository.java 
  → repository/postgres/UserRepository.java

repository/ProductRepository.java 
  → repository/postgres/ProductRepository.java
```

**Para SQL Server:**
```
repository/ExternalRepository.java 
  → repository/sqlserver/ExternalRepository.java
```

#### Opción B: Usar refactoring de IDE

1. Click derecho en el repositorio
2. Refactor → Move
3. Seleccionar el paquete destino: 
   - `com.eljumillano.loop.repository.postgres` (para PostgreSQL)
   - `com.eljumillano.loop.repository.sqlserver` (para SQL Server)

### 2️⃣ Actualizar Imports en Servicios

Después de mover los repositorios, actualiza los imports en tus servicios:

**Antes:**
```java
import com.eljumillano.loop.repository.UserRepository;
```

**Después:**
```java
import com.eljumillano.loop.repository.postgres.UserRepository;
// o
import com.eljumillano.loop.repository.sqlserver.ExternalRepository;
```

### 3️⃣ Configurar Variables de Entorno

Asegúrate de que tu archivo `.env` tenga las variables necesarias:

```properties
# PostgreSQL (Base de datos principal)
DB_HOST=192.168.0.160
DB_PORT=5432
DB_NAME=loop_db
DB_USER=usuario1
DB_PASSWORD=Piramide83

# SQL Server (Base de datos secundaria)
SQLSERVER_HOST=192.168.0.234
SQLSERVER_PORT=1433
SQLSERVER_DB=H2O_JUMI
SQLSERVER_USER=h2o
SQLSERVER_PASSWORD=Jumi1234
```

### 4️⃣ Ejemplo de Repositorio para Cada BD

#### PostgreSQL Repository
```java
package com.eljumillano.loop.repository.postgres;

import com.eljumillano.loop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos personalizados
}
```

#### SQL Server Repository
```java
package com.eljumillano.loop.repository.sqlserver;

import com.eljumillano.loop.model.ExternalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalDataRepository extends JpaRepository<ExternalData, Long> {
    // Métodos personalizados
}
```

## 📊 Uso en Servicios

### Inyección de Repositorios

```java
@Service
public class MiServicio {
    
    // Repository de PostgreSQL
    private final UserRepository userRepository;
    
    // Repository de SQL Server
    private final ExternalDataRepository externalDataRepository;
    
    public MiServicio(
            UserRepository userRepository,
            ExternalDataRepository externalDataRepository) {
        this.userRepository = userRepository;
        this.externalDataRepository = externalDataRepository;
    }
    
    public void procesarDatos() {
        // Usa PostgreSQL
        User user = userRepository.findById(1L).orElse(null);
        
        // Usa SQL Server
        ExternalData data = externalDataRepository.findById(1L).orElse(null);
    }
}
```

### Transacciones con Múltiples Datasources

```java
@Service
public class TransaccionalService {
    
    // Transacción específica para PostgreSQL
    @Transactional("postgresTransactionManager")
    public void guardarEnPostgres(User user) {
        userRepository.save(user);
    }
    
    // Transacción específica para SQL Server
    @Transactional("sqlServerTransactionManager")
    public void guardarEnSqlServer(ExternalData data) {
        externalDataRepository.save(data);
    }
    
    // IMPORTANTE: No puedes tener una transacción que use AMBAS bases de datos
    // Si necesitas esto, considera usar patrón Saga o eventos
}
```

## ⚠️ Consideraciones Importantes

### ✅ Buenas Prácticas

1. **Separación clara**: Mantén los repositorios organizados por BD
2. **Naming convention**: Usa nombres descriptivos para los repositorios
3. **Transacciones explícitas**: Especifica el transaction manager cuando sea necesario
4. **Testing**: Mockea los repositorios o usa bases embebidas para tests

### ❌ Limitaciones

1. **No transacciones distribuidas**: No puedes tener una transacción que modifique ambas BDs a la vez
2. **Performance**: Dos conexiones implica más recursos
3. **Complejidad**: Más configuración y mantenimiento

### 🔍 Debugging

Si tienes problemas, verifica:

1. **Logs de inicio**: Spring debe mostrar ambos datasources configurados
2. **Package scan**: Asegúrate que `basePackages` apunta a los paquetes correctos
3. **Entity scan**: Verifica que las entidades estén en los paquetes configurados
4. **Variables de entorno**: Confirma que todas las variables están definidas

## 🧪 Testing

### Test con DataSource específico

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    
    @Autowired
    @Qualifier("postgresEntityManagerFactory")
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void testFindById() {
        // Test usando PostgreSQL
        User user = new User();
        userRepository.save(user);
        assertNotNull(userRepository.findById(user.getId()));
    }
}
```

## 📝 Distribución Sugerida de Repositorios

### PostgreSQL (Primary) - Datos propios de la aplicación
- ✅ UserRepository
- ✅ OrderRepository
- ✅ OrderItemRepository
- ✅ ProductRepository
- ✅ SucursalRepository
- ✅ ControlRepository
- ✅ ControlProductRepository
- ✅ PackingSlipRepository
- ✅ DisposableRepository

### SQL Server (Secondary) - Datos de sistemas externos
- ✅ Repositorios que consulten H2O_JUMI u otras bases externas
- ✅ Repositorios de solo lectura para reportes
- ✅ Integración con sistemas legacy

## 🔧 Troubleshooting

### Error: "Cannot determine target DataSource"
**Solución**: Asegúrate de tener `@Primary` en la configuración de PostgreSQL

### Error: "No qualifying bean of type EntityManagerFactory"
**Solución**: Verifica que los nombres de los beans son correctos en `@Qualifier`

### Error: "Table doesn't exist"
**Solución**: Verifica que `hibernate.hbm2ddl.auto` esté configurado correctamente

### Error: "Multiple DataSources found"
**Solución**: Usa `@Qualifier` para especificar qué datasource inyectar

## 🎯 Próximos Pasos

1. ✅ Decidir qué repositorios van a cada base de datos
2. ✅ Crear los paquetes `repository.postgres` y `repository.sqlserver`
3. ✅ Mover los repositorios a sus respectivos paquetes
4. ✅ Actualizar los imports en los servicios
5. ✅ Probar la conexión a ambas bases de datos
6. ✅ Crear entidades si son necesarias para SQL Server

## 📚 Recursos Adicionales

- [Spring Boot Multiple Databases](https://www.baeldung.com/spring-boot-configure-multiple-datasources)
- [JPA Multiple Persistence Units](https://www.baeldung.com/jpa-multiple-db)
- [Spring Data JPA with Multiple Databases](https://spring.io/guides/gs/accessing-data-jpa/)

---
**Creado para:** Loop Application  
**Fecha:** Abril 2026
