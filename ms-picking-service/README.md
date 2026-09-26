# ms-picking-service

Microservicio de picking y despacho de BodegaNube.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Responsabilidad

- Listar al operario de bodega las órdenes disponibles para picking (con stock ya reservado por `ms-inventario`).
- Registrar el picking y el despacho de una orden.
- Generar y registrar el número de seguimiento de cada pedido despachado.

## Endpoints

```http
GET  /picking/ordenes
POST /picking/ordenes/{id}/despacho
```

## Reglas de negocio

- Solo se muestran para picking las órdenes con stock reservado.
- Al registrar el despacho se genera un número de seguimiento único.

## Roles

```text
OPERARIO
```

## Base de datos

PostgreSQL (`ms_picking`).

## Variables de entorno

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
MS_ORDENES_URL=
```

## Ejecución local

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```
