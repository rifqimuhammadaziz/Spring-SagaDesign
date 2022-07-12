package rifqimuhammadaziz.CommonService.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import rifqimuhammadaziz.CommonService.model.CardDetails;

import java.lang.annotation.Target;

@Data
@Builder
public class ValidatePaymentCommand {

    @TargetAggregateIdentifier
    private String paymentId;
    private String orderId;
    private CardDetails cardDetails;
}
