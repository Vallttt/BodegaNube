package cl.duoc.ms_picking_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ordenes_picking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenPicking {

    @Id
    private String id;

    @Indexed(unique = true)
    private String ordenId;        // id de la orden en ms-ordenes (no puede repetirse)

    private String comercioId;     // comercio dueño de la orden
    private EstadoPicking estado;
    private String numeroSeguimiento;
    private LocalDateTime fechaDisponible;
    private LocalDateTime fechaDespacho;

    public static OrdenPicking nuevaDisponible(String ordenId, String comercioId) {
        return OrdenPicking.builder()
                .ordenId(ordenId)
                .comercioId(comercioId)
                .estado(EstadoPicking.DISPONIBLE)
                .fechaDisponible(LocalDateTime.now())
                .build();
    }
}