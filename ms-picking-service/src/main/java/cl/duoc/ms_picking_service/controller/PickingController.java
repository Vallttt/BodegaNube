package cl.duoc.ms_picking_service.controller;

import cl.duoc.ms_picking_service.dto.DespachoResponse;
import cl.duoc.ms_picking_service.dto.OrdenDisponibleRequest;
import cl.duoc.ms_picking_service.dto.OrdenPickingResponse;
import cl.duoc.ms_picking_service.service.PickingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/picking")
@RequiredArgsConstructor
public class PickingController {

    private final PickingService pickingService;

    // GET /picking/ordenes -> lista las ordenes disponibles para picking (operario)
    @GetMapping("/ordenes")
    public ResponseEntity<List<OrdenPickingResponse>> listarDisponibles() {
        return ResponseEntity.ok(pickingService.listarDisponibles());
    }

    // POST /picking/ordenes -> uso interno, invocado por ms-ordenes cuando la orden ya tiene stock reservado
    @PostMapping("/ordenes")
    public ResponseEntity<OrdenPickingResponse> registrarOrdenDisponible(
            @Valid @RequestBody OrdenDisponibleRequest request) {
        OrdenPickingResponse response = pickingService.registrarOrdenDisponible(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // POST /picking/ordenes/{id}/despacho -> el operario registra el despacho y se genera el N de seguimiento
    @PostMapping("/ordenes/{ordenId}/despacho")
    public ResponseEntity<DespachoResponse> registrarDespacho(@PathVariable String ordenId) {
        return ResponseEntity.ok(pickingService.registrarDespacho(ordenId));
    }
}