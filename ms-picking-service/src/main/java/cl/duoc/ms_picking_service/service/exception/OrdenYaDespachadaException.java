package cl.duoc.ms_picking_service.service.exception;

public class OrdenYaDespachadaException extends RuntimeException {
    public OrdenYaDespachadaException(String ordenId) {
        super("La orden ordenId=" + ordenId + " ya fue despachada");
    }
}