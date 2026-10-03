package cl.duoc.ms_picking_service.repository;

import cl.duoc.ms_picking_service.model.EstadoPicking;
import cl.duoc.ms_picking_service.model.OrdenPicking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenPickingRepository extends MongoRepository<OrdenPicking, String> {

    List<OrdenPicking> findByEstado(EstadoPicking estado);

    Optional<OrdenPicking> findByOrdenId(String ordenId);

    boolean existsByOrdenId(String ordenId);
}