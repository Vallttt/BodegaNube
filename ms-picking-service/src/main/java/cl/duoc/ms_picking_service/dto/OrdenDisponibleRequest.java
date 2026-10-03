package cl.duoc.ms_picking_service.dto;

import jakarta.validation.constraints.NotBlank;

public record OrdenDisponibleRequest(
        @NotBlank(message = "ordenId es obligatorio")
        String ordenId,

        @NotBlank(message = "comercioId es obligatorio")
        String comercioId
) {
}