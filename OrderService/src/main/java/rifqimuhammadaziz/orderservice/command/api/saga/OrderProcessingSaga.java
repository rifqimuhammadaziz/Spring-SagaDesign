package rifqimuhammadaziz.orderservice.command.api.saga;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import rifqimuhammadaziz.CommonService.command.CompleteOrderCommand;
import rifqimuhammadaziz.CommonService.command.ShipOrderCommand;
import rifqimuhammadaziz.CommonService.command.ValidatePaymentCommand;
import rifqimuhammadaziz.CommonService.events.OrderCompletedEvent;
import rifqimuhammadaziz.CommonService.events.OrderShippedEvent;
import rifqimuhammadaziz.CommonService.events.PaymentProcessedEvent;
import rifqimuhammadaziz.CommonService.model.User;
import rifqimuhammadaziz.CommonService.queries.GetUserPaymentDetailsQuery;
import rifqimuhammadaziz.orderservice.command.api.events.OrderCreatedEvent;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order ID: {}", event.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery =
                new GetUserPaymentDetailsQuery(event.getUserId());

        User user = null;
        try {
            user = queryGateway.query(
                    getUserPaymentDetailsQuery,
                    ResponseTypes.instanceOf(User.class)
            ).join();
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensating transaction
        }

        ValidatePaymentCommand validatePaymentCommand =
                ValidatePaymentCommand.builder()
                        .cardDetails(user.getCardDetails())
                        .orderId(event.getOrderId())
                        .paymentId(UUID.randomUUID().toString())
                        .build();

        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Saga for Order ID: {}", event.getOrderId());
        try {
            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .shipmentId(UUID.randomUUID().toString())
                    .orderId(event.getOrderId())
                    .build();

            commandGateway.send(shipOrderCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            // Start the compensation transaction
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent event) {
        log.info("OrderShippedEvent in Saga for Order ID: {}", event.getOrderId());
        CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                .orderId(event.getOrderId())
                .orderStatus("APPROVED")
                .build();
        commandGateway.send(completeOrderCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for Order ID: {}", event.getOrderId());
    }
}
