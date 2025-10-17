# Java Spring Boot Microservices (Maven + H2)
Repositorio con dos microservicios implementados en **Java 17 + Spring Boot 3**:
- `products-service` (puerto 8081)
- `inventory-service` (puerto 8082)

Cada servicio usa **H2 (file)** como base de datos para facilidad de entrega y pruebas.
Comunicación entre servicios vía HTTP (JSON API minimal) y autenticación por header `X-API-Key`.

## Contenido
- products-service/: código fuente, Dockerfile, pom.xml
- inventory-service/: código fuente, Dockerfile, pom.xml
- docker-compose.yml: orquesta ambos servicios

## Ejecutar con Docker Compose
```bash
docker-compose up --build
```

Products: http://localhost:8081/swagger-ui.html  
Inventory: http://localhost:8082/swagger-ui.html

## Ejecutar localmente (sin Docker)
Requisitos: Java 17, Maven
```bash
# products
cd products-service
mvn clean spring-boot:run

# inventory
cd ../inventory-service
mvn clean spring-boot:run
```

## Tests
Cada servicio incluye pruebas básicas con JUnit + Spring Test.
Ejecutar dentro del módulo:
```bash
mvn test
```

## Notas
- El endpoint de compra (`POST /purchase`) está implementado en `inventory-service`.
- Respuestas siguen un JSON API mínimo: `{ "data": { "type": "...", "id": "...", "attributes": { ... } } }`
- Para producción se recomienda Postgres, resiliencia (retries/backoff), y mecanismos para evitar race conditions.