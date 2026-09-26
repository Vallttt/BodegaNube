# BodegaNube

Plataforma que gestiona los pedidos de tiendas que venden por internet (Shopify u otro canal similar). Cuando un cliente compra en el canal externo, el sistema recibe un aviso vía webhook, lo encola de forma asíncrona y luego lo procesa: crea la orden, reserva stock y deja el pedido disponible para picking y despacho.

## Arquitectura

```
Canal externo (Shopify, etc.)
        │  webhook (venta)
        ▼
  Amazon API Gateway  ───────────────► respuesta inmediata (202 Accepted)
        │
        ▼
   Amazon SQS (cola de avisos)
        │
        ▼
    AWS Lambda (procesamiento asíncrono)
        │
        ├──► ms-ordenes        (crea la orden, evita duplicados)
        └──► ms-inventario     (verifica y reserva stock)

  Operario de bodega ──► API Gateway (JWT) ──► ms-picking-service
  Comercio            ──► API Gateway (JWT) ──► ms-ordenes (solo sus propias órdenes)
  Login/roles         ──► API Gateway (JWT) ──► ms-auth
```

El webhook y el acceso de los usuarios pasan siempre por **Amazon API Gateway**; ningún microservicio queda expuesto directamente a Internet.

## Microservicios

| Servicio | Carpeta | Responsabilidad | Puerto |
|---|---|---|---|
| Autenticación y usuarios | [`ms-auth`](./ms-auth) | Login, roles y autorización (Operario / Comercio) | 8081 |
| Órdenes | [`ms-ordenes`](./ms-ordenes) | Crear órdenes desde el webhook, evitar duplicados, consulta por comercio | 8082 |
| Inventario | [`ms-inventario`](./ms-inventario) | Catálogo de productos, verificación y reserva de stock | 8083 |
| Picking y despacho | [`ms-picking-service`](./ms-picking-service) | Órdenes disponibles para picking, registro de despacho y N° de seguimiento | 8084 |

## Componentes serverless

| Componente | Carpeta | Función |
|---|---|---|
| Webhook Lambda | [`lambda-bodeganube-webhook`](./lambda-bodeganube-webhook) | Procesa asíncronamente los mensajes de la cola SQS y orquesta `ms-ordenes` / `ms-inventario` |

## Infraestructura

Ver [`infra-bodeganube`](./infra-bodeganube) para el `docker-compose` de desarrollo local y la configuración de API Gateway.

## Roles

- **Operario de bodega**: consulta órdenes disponibles, realiza el picking y registra el despacho.
- **Comercio**: consulta únicamente sus propias órdenes y su estado.

## Documentación

- [`docs`](./docs) — diagrama de arquitectura y caso de estudio.
