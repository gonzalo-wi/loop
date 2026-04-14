# Integración con Feign Client - Servicio de Reparto

## 📋 Descripción
Implementación de cliente Feign para consumir el servicio externo de reparto con buenas prácticas de Spring Boot.

## 🏗️ Arquitectura

```
client/
  └── DeliveryServiceClient.java      # Interface Feign Client
config/
  ├── FeignClientConfig.java          # Configuración global de Feign
  └── CustomFeignErrorDecoder.java    # Manejo de errores personalizado
dtos/delivery/
  ├── StockItemDTO.java               # DTO para items de stock
  └── StockResponseDTO.java           # DTO para respuesta completa
service/
  └── DeliveryService.java            # Lógica de negocio
controller/
  └── DeliveryController.java         # Endpoints REST
```

## 🚀 Características Implementadas

### ✅ Buenas Prácticas Aplicadas

1. **Separación de responsabilidades**
   - Cliente Feign (interfaz) solo define el contrato
   - Servicio contiene la lógica de negocio
   - Controlador maneja las peticiones HTTP

2. **Manejo de errores robusto**
   - ErrorDecoder personalizado para errores específicos
   - Logging detallado de errores
   - Respuestas HTTP apropiadas

3. **Configuración centralizada**
   - Timeouts configurables
   - Estrategia de reintento
   - URL externalizada en properties

4. **Resiliencia**
   - 3 reintentos automáticos con backoff
   - Connection timeout: 10 segundos
   - Read timeout: 60 segundos

5. **Observabilidad**
   - Logging completo (FULL) de requests/responses
   - SLF4J para trazabilidad
   - Logs estructurados

6. **DTOs bien definidos**
   - Mapeo de JSON con @JsonProperty
   - Nombres descriptivos en español
   - Constructores y toString()

## 📝 Uso

### En un Servicio
```java
@Service
public class MiServicio {
    private final DeliveryService deliveryService;
    
    public void procesarReparto(Long idReparto) {
        // Obtener stock
        StockResponseDTO stock = deliveryService.obtenerStockPorReparto(idReparto);
        
        // Procesar datos
        stock.getData().forEach(item -> {
            System.out.println("Producto: " + item.getAbreviaturaProducto());
            System.out.println("Bultos: " + item.getCantidadBultosCargo());
        });
    }
}
```

### Desde el Controlador REST
```bash
# Obtener stock de reparto ID 10
GET http://localhost:8080/api/delivery/10/stock

# Verificar si tiene stock
GET http://localhost:8080/api/delivery/10/has-stock
```

## ⚙️ Configuración

### application.properties
```properties
# URL del servicio externo
delivery.service.url=http://jmobile.somoselagua.com.ar:8080

# Nivel de log (DEBUG para desarrollo, INFO para producción)
logging.level.com.eljumillano.loop.client=DEBUG
```

### Variables de entorno (opcional)
```properties
# En .env
DELIVERY_SERVICE_URL=http://jmobile.somoselagua.com.ar:8080
```

```properties
# En application.properties
delivery.service.url=${DELIVERY_SERVICE_URL}
```

## 🔧 Personalización

### Cambiar Timeouts
En `FeignClientConfig.java`:
```java
@Bean
public Request.Options requestOptions() {
    return new Request.Options(
        5, TimeUnit.SECONDS,   // Connection timeout más corto
        30, TimeUnit.SECONDS,  // Read timeout más corto
        true
    );
}
```

### Cambiar Estrategia de Reintento
```java
@Bean
public Retryer retryer() {
    return new Retryer.Default(
        200,    // intervalo inicial de 200ms
        2000,   // máximo intervalo de 2s
        5       // 5 intentos máximo
    );
}
```

### Deshabilitar Reintentos
```java
@Bean
public Retryer retryer() {
    return Retryer.NEVER_RETRY;
}
```

## 🧪 Testing

### Test Unitario del Servicio
```java
@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {
    
    @Mock
    private DeliveryServiceClient client;
    
    @InjectMocks
    private DeliveryService service;
    
    @Test
    void obtenerStockPorReparto_Success() {
        // Arrange
        Long idReparto = 10L;
        StockResponseDTO mockResponse = new StockResponseDTO();
        when(client.getStockByDelivery(idReparto)).thenReturn(mockResponse);
        
        // Act
        StockResponseDTO result = service.obtenerStockPorReparto(idReparto);
        
        // Assert
        assertNotNull(result);
        verify(client).getStockByDelivery(idReparto);
    }
}
```

## 📊 Monitoreo

### Ver logs de Feign
Los logs incluyen:
- URL completa llamada
- Headers enviados y recibidos
- Body de request y response
- Tiempo de respuesta
- Errores y stack traces

### Metricas con Actuator
Agregar en `pom.xml` si no está:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Acceder a: `http://localhost:8080/actuator/metrics`

## 🔐 Seguridad

### Agregar Autenticación (si es necesario)
```java
@Configuration
public class FeignClientConfig {
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer YOUR_TOKEN");
            requestTemplate.header("X-Api-Key", "YOUR_API_KEY");
        };
    }
}
```

## 🎯 Ventajas de esta Implementación

1. ✅ **Mantenible**: Código limpio y organizado
2. ✅ **Resiliente**: Maneja errores y reintentos automáticos
3. ✅ **Testeable**: Fácil de mockear y probar
4. ✅ **Configurable**: Sin hardcodear valores
5. ✅ **Observable**: Logs detallados para debugging
6. ✅ **Escalable**: Fácil agregar más endpoints
7. ✅ **Documentado**: Javadoc en cada componente

## 📚 Recursos Adicionales

- [Spring Cloud OpenFeign Docs](https://spring.io/projects/spring-cloud-openfeign)
- [Feign GitHub](https://github.com/OpenFeign/feign)
- [Best Practices for Feign](https://www.baeldung.com/spring-cloud-openfeign)

---
**Creado para:** Loop Application  
**Fecha:** Abril 2026
