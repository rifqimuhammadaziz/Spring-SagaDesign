package rifqimuhammadaziz.orderservice.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rifqimuhammadaziz.CommonService.events.OrderCompletedEvent;
import rifqimuhammadaziz.orderservice.command.api.data.Order;
import rifqimuhammadaziz.orderservice.command.api.data.OrderRepository;

@Component
public class OrderEventsHandler {

    @Autowired
    private OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).get();
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }
}
