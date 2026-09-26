package cl.duoc.lambda_webhook;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

/**
 * Handler de AWS Lambda que procesa de forma asíncrona los avisos de venta
 * recibidos desde Amazon SQS (encolados a partir del webhook del canal externo).
 *
 * Responsabilidades (a implementar):
 *  - Deserializar el mensaje del webhook.
 *  - Validar duplicados junto a ms-ordenes.
 *  - Solicitar reserva de stock a ms-inventario.
 *  - Crear la orden en ms-ordenes si hay stock disponible.
 *
 * Estructura únicamente — sin lógica de negocio implementada.
 */
public class WebhookProcesarHandler implements RequestHandler<SQSEvent, Void> {

	@Override
	public Void handleRequest(SQSEvent event, Context context) {
		// TODO: procesar cada SQSEvent.SQSMessage del batch.
		return null;
	}
}
