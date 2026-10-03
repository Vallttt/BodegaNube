# BodegaNube

Plataforma que gestiona los pedidos de tiendas que venden por internet (Shopify u otro canal similar). Cuando un cliente compra en el canal externo, el sistema recibe un aviso vía webhook, lo encola de forma asíncrona y luego lo procesa: reserva stock, crea la orden y deja el pedido disponible para picking y despacho.

## Arquitectura

Diagrama fuente: [`docs/diagrama/BodegaNube_Arquitectura.puml`](./docs/diagrama/BodegaNube_Arquitectura.puml)

```
 Comercio ──┐
 Operario ──┼──► Amazon API Gateway ──► (síncrono) ──┬──► ms-auth
             │                                        ├──► ms-ordenes      (consulta)
 Canal       │                                        └──► ms-picking-service
 externo ────┘
   │ webhook (asíncrono)
   ▼
 Amazon API Gateway ──► Amazon SQS ──► AWS Lambda
                             │              │
                             ▼              ├─ 1. reserva stock ──► ms-inventario
                            DLQ              └─ 2. crea orden / dedup ──► ms-ordenes
                     (reintentos                 (solo si hubo stock)
                      agotados,
                      fallo técnico)
```

El webhook y el acceso de los usuarios pasan siempre por **Amazon API Gateway**; ningún microservicio queda expuesto directamente a Internet.

La **Lambda orquesta de forma secuencial**: primero reserva el stock en `ms-inventario` y solo si hay stock disponible crea la orden en `ms-ordenes`. Si no hay stock, es un resultado de negocio (no un error técnico): la Lambda confirma el mensaje SQS como procesado y **no** lo reintenta ni lo envía a la DLQ. Los avisos de venta que sí fallan por un problema técnico (timeout, servicio caído, etc.) agotan sus reintentos y terminan en la **Dead Letter Queue (DLQ)**.

## Microservicios

| Servicio | Carpeta | Responsabilidad | Base de datos | Puerto |
|---|---|---|---|---|
| Autenticación y usuarios | [`ms-auth`](./ms-auth) | Login, roles y autorización (Operario / Comercio) | PostgreSQL | 8081 |
| Órdenes | [`ms-ordenes`](./ms-ordenes) | Crear órdenes desde la Lambda (ya con stock reservado), evitar duplicados, consulta por comercio | PostgreSQL | 8082 |
| Inventario | [`ms-inventario`](./ms-inventario) | Catálogo de productos, verificación y reserva de stock | PostgreSQL | 8083 |
| Picking y despacho | [`ms-picking-service`](./ms-picking-service) | Órdenes disponibles para picking, registro de despacho y N° de seguimiento | MongoDB | 8084 |

## Componentes serverless

| Componente | Carpeta | Función |
|---|---|---|
| Webhook Lambda | [`lambda-bodeganube-webhook`](./lambda-bodeganube-webhook) | Procesa asíncronamente los mensajes de Amazon SQS: reserva stock en `ms-inventario` y, si corresponde, crea la orden en `ms-ordenes` |

## Infraestructura

Ver [`infra-bodeganube`](./infra-bodeganube) para el `docker-compose` de desarrollo local y la configuración de API Gateway (SQS, DLQ, rutas).

## Roles

- **Operario de bodega**: consulta órdenes disponibles, realiza el picking y registra el despacho.
- **Comercio**: consulta únicamente sus propias órdenes y su estado.

## Documentación

- [`docs`](./docs) — diagrama de arquitectura (`.puml` + imagen) y caso de estudio.
