package cl.duoc.ms_picking_service.dto;

import cl.duoc.ms_picking_service.model.EstadoPicking;
import cl.duoc.ms_picking_service.model.OrdenPicking;

import java.time.LocalDateTime;

public record OrdenPickingResponse(
        String ordenId,
        String comercioId,
        EstadoPicking estado,
        LocalDateTime fechaDisponible
) {
    public static OrdenPickingResponse desde(OrdenPicking orden) {
        return new OrdenPickingResponse(
                orden.getOrdenId(),
                orden.getComercioId(),
                orden.getEstado(),
                orden.getFechaDisponible()
        );
    }
}