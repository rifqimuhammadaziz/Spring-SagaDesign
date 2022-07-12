package rifqimuhammadaziz.ShipmentService.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rifqimuhammadaziz.CommonService.events.OrderShippedEvent;
import rifqimuhammadaziz.ShipmentService.command.api.data.Shipment;
import rifqimuhammadaziz.ShipmentService.command.api.data.ShipmentRepository;

@Component
public class ShipmentsEventHandler {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment = new Shipment();
        BeanUtils.copyProperties(event, shipment);
        shipmentRepository.save(shipment);
    }
}
