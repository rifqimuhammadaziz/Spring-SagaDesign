package rifqimuhammadaziz.orderservice.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import rifqimuhammadaziz.CommonService.command.CompleteOrderCommand;
import rifqimuhammadaziz.CommonService.events.OrderCompletedEvent;
import rifqimuhammadaziz.orderservice.command.api.command.CreateOrderCommand;
import rifqimuhammadaziz.orderservice.command.api.events.OrderCreatedEvent;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        // Validate Command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderStatus = event.getOrderStatus();
        this.userId = event.getUserId();
        this.orderId = event.getOrderId();
        this.quantity = event.getQuantity();
        this.productId = event.getProductId();
        this.addressId = event.getAddressId();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        // Validate the command
        // Publish Order Completed Event
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
                .orderStatus(completeOrderCommand.getOrderStatus())
                .orderId(completeOrderCommand.getOrderId())
                .build();
        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderStatus = event.getOrderStatus();

    }
}
