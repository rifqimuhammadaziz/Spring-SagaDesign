package rifqimuhammadaziz.PaymentService.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rifqimuhammadaziz.CommonService.events.PaymentCancelledEvent;
import rifqimuhammadaziz.CommonService.events.PaymentProcessedEvent;
import rifqimuhammadaziz.PaymentService.command.api.data.Payment;
import rifqimuhammadaziz.PaymentService.command.api.data.PaymentRepository;

import java.util.Date;

@Component
public class PaymentsEventHandler {

    @Autowired
    private PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timestamp(new Date())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId()).get();
        payment.setPaymentStatus(event.getPaymentStatus());

        paymentRepository.save(payment);
    }
}
