# lambda-bodeganube-webhook

Función AWS Lambda que procesa de forma asíncrona los avisos de venta encolados en Amazon SQS.

## Tecnologías

- Java 21
- AWS Lambda (`aws-lambda-java-core`, `aws-lambda-java-events`)
- Amazon SQS (trigger)

## Flujo

```
Amazon API Gateway → Amazon SQS (bodeganube-avisos-venta) → AWS Lambda
        │
        ├──► ms-ordenes      (crear orden / evitar duplicados)
        └──► ms-inventario   (verificar y reservar stock)
```

## Responsabilidades

- Recibir el batch de mensajes SQS (`WebhookProcesarHandler`).
- Validar duplicados de la orden.
- Reservar stock en `ms-inventario`.
- Crear la orden en `ms-ordenes` si hay stock disponible.

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
