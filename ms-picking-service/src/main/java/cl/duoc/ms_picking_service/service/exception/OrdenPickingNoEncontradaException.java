package cl.duoc.ms_picking_service.service.exception;

public class OrdenPickingNoEncontradaException extends RuntimeException {
    public OrdenPickingNoEncontradaException(String ordenId) {
        super("No se encontró una orden de picking para ordenId=" + ordenId);
    }
}