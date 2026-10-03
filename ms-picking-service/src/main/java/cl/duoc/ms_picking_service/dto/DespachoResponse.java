package cl.duoc.ms_picking_service.dto;

import java.time.LocalDateTime;

public record DespachoResponse(
        String ordenId,
        String numeroSeguimiento,
        LocalDateTime fechaDespacho
) {
}