package cl.duoc.ms_picking_service.service;

import cl.duoc.ms_picking_service.dto.DespachoResponse;
import cl.duoc.ms_picking_service.dto.OrdenDisponibleRequest;
import cl.duoc.ms_picking_service.dto.OrdenPickingResponse;
import cl.duoc.ms_picking_service.model.EstadoPicking;
import cl.duoc.ms_picking_service.model.OrdenPicking;
import cl.duoc.ms_picking_service.repository.OrdenPickingRepository;
import cl.duoc.ms_picking_service.service.exception.OrdenPickingNoEncontradaException;
import cl.duoc.ms_picking_service.service.exception.OrdenPickingYaExisteException;
import cl.duoc.ms_picking_service.service.exception.OrdenYaDespachadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PickingService {

    private final OrdenPickingRepository repository;

    public List<OrdenPickingResponse> listarDisponibles() {
        return repository.findByEstado(EstadoPicking.DISPONIBLE)
                .stream()
                .map(OrdenPickingResponse::desde)
                .toList();
    }

    public OrdenPickingResponse registrarOrdenDisponible(OrdenDisponibleRequest request) {
        if (repository.existsByOrdenId(request.ordenId())) {
            throw new OrdenPickingYaExisteException(request.ordenId());
        }
        OrdenPicking orden = OrdenPicking.nuevaDisponible(request.ordenId(), request.comercioId());
        return OrdenPickingResponse.desde(repository.save(orden));
    }

    public DespachoResponse registrarDespacho(String ordenId) {
        OrdenPicking orden = repository.findByOrdenId(ordenId)
                .orElseThrow(() -> new OrdenPickingNoEncontradaException(ordenId));

        if (orden.getEstado() == EstadoPicking.DESPACHADO) {
            throw new OrdenYaDespachadaException(ordenId);
        }

        orden.setEstado(EstadoPicking.DESPACHADO);
        orden.setNumeroSeguimiento(generarNumeroSeguimiento());
        orden.setFechaDespacho(LocalDateTime.now());
        repository.save(orden);

        return new DespachoResponse(orden.getOrdenId(), orden.getNumeroSeguimiento(), orden.getFechaDespacho());
    }

    private String generarNumeroSeguimiento() {
        return "BN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}