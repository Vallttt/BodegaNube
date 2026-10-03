# ms-picking-service

Microservicio de picking y despacho de BodegaNube.

## Tecnologías

- Java 21
- Spring Boot
- Spring Data MongoDB
- MongoDB

## Responsabilidad

- Recibir la notificación de que una orden quedó disponible para picking (uso interno, invocado por `ms-ordenes` una vez que el stock ya fue reservado).
- Listar al operario de bodega las órdenes disponibles para picking.
- Registrar el despacho de una orden y generar su número de seguimiento.

## Endpoints

```http
POST /picking/ordenes                 (uso interno, invocado por ms-ordenes)
GET  /picking/ordenes
POST /picking/ordenes/{ordenId}/despacho
```

## Reglas de negocio

- No se puede registrar dos veces una orden de picking para el mismo `ordenId` (409 Conflict).
- Solo se listan para picking las órdenes en estado `DISPONIBLE`.
- Una orden ya despachada no puede volver a despacharse (409 Conflict).
- Al registrar el despacho se genera un número de seguimiento único (`BN-XXXXXXXX`).

## Roles

```text
OPERARIO
```

## Base de datos

MongoDB (`ms_picking`).

## Variables de entorno

```env
MONGODB_URI=
```

## Ejecución local

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```