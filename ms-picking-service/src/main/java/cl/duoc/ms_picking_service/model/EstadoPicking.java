package cl.duoc.ms_picking_service.model;


public enum EstadoPicking {
    DISPONIBLE,   // la orden tiene stock reservado y espera ser pickeada
    EN_PICKING,   // el operario la tomó para preparar
    DESPACHADO    // ya se registró el despacho y tiene N° de seguimiento

}
