package cl.duoc.ms_picking_service.service.exception;

public class OrdenPickingYaExisteException extends RuntimeException {
    public OrdenPickingYaExisteException(String ordenId) {
        super("Ya existe una orden de picking para ordenId=" + ordenId);
    }
}