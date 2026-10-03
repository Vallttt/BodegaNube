# ms-ordenes

Microservicio de órdenes de BodegaNube.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL

## Responsabilidad

- Crear la orden a partir del aviso de venta procesado por la Lambda (`lambda-bodeganube-webhook`), **una vez que el stock ya fue reservado** por `ms-inventario`, evitando duplicados.
- Permitir que el comercio consulte únicamente sus propias órdenes y su estado (vía API Gateway, síncrono).
- Dejar disponible la orden para `ms-picking-service`.

> Nota de diseño: `ms-ordenes` no llama a `ms-inventario`. La reserva de stock la orquesta la Lambda **antes** de pedir la creación de la orden — así se evita el acoplamiento síncrono ambiguo entre ambos microservicios.

## Endpoints

```http
GET  /ordenes
GET  /ordenes/{id}
POST /ordenes            (uso interno, invocado por la Lambda)
```

## Reglas de negocio

- Un mismo aviso de venta (idempotency key del canal externo) no puede generar dos órdenes.
- Solo se crea la orden si la Lambda confirma que el stock fue reservado.
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
```

## Ejecución local

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```
