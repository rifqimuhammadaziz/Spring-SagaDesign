package rifqimuhammadaziz.ShipmentService.command.api.data;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
