# lambda-bodeganube-webhook

Función AWS Lambda que procesa de forma asíncrona los avisos de venta encolados en Amazon SQS.

## Tecnologías

- Java 21
- AWS Lambda (`aws-lambda-java-core`, `aws-lambda-java-events`)
- Amazon SQS (trigger) + DLQ

## Flujo (orquestación secuencial)

```
Amazon API Gateway → Amazon SQS (bodeganube-avisos-venta) → AWS Lambda

AWS Lambda
   1. reserva stock  ──► ms-inventario
        │
        ├─ sin stock  → resultado de negocio: no se crea la orden,
        │                se confirma el mensaje SQS (no reintenta, no va a DLQ)
        │
        └─ con stock  → 2. crea orden / evita duplicados ──► ms-ordenes

DLQ (bodeganube-avisos-venta-dlq) ← solo fallos técnicos (timeout, servicio caído, etc.)
```

## Responsabilidades

- Recibir el batch de mensajes SQS (`WebhookProcesarHandler`).
- Reservar stock en `ms-inventario` **antes** de crear la orden.
- Si no hay stock: registrar el rechazo como resultado de negocio y confirmar el mensaje (no se reintenta).
- Si hay stock: crear la orden en `ms-ordenes`, validando duplicados.
- Dejar que el mensaje vuelva a SQS (y eventualmente a la DLQ) **solo** cuando el fallo es técnico, no cuando es de negocio (sin stock).

## Variables de entorno

```env
MS_ORDENES_URL=
MS_INVENTARIO_URL=
```

## Build

```bash
mvn clean package
```

Genera el `.jar` a subir como código de la función Lambda.
