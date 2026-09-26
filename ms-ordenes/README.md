# ms-ordenes

Microservicio de órdenes de BodegaNube.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Responsabilidad

- Crear la orden a partir del aviso de venta procesado por la Lambda (`lambda-bodeganube-webhook`), evitando duplicados.
- Permitir que el comercio consulte únicamente sus propias órdenes y su estado.
- Notificar a `ms-picking-service` cuando una orden tiene stock reservado y queda disponible para picking.

## Endpoints

```http
GET  /ordenes
GET  /ordenes/{id}
POST /ordenes            (uso interno, invocado por la Lambda)
```

## Reglas de negocio

- Un mismo aviso de venta (idempotency key del canal externo) no puede generar dos órdenes.
- Una orden solo queda disponible para picking si `ms-inventario` confirmó la reserva de stock.
- El comercio solo puede ver las órdenes asociadas a su propio identificador de comercio.

## Roles

```text
COMERCIO   (consulta sus propias órdenes)
OPERARIO   (consulta a través de ms-picking-service)
```

## Base de datos

PostgreSQL (`ms_ordenes`).

## Variables de entorno

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
MS_INVENTARIO_URL=
```

## Ejecución local

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```
