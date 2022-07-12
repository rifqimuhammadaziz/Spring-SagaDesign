package rifqimuhammadaziz.UserService.projection;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import rifqimuhammadaziz.CommonService.model.CardDetails;
import rifqimuhammadaziz.CommonService.model.User;
import rifqimuhammadaziz.CommonService.queries.GetUserPaymentDetailsQuery;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
        // Ideally get the details from db
        CardDetails cardDetails = CardDetails.builder()
                .name("Rifqi Muhammad Aziz")
                .cardNumber("3376 0982 2883 1828")
                .validUntilYear(2022)
                .validUntilMonth(02)
                .cvv(998)
                .build();

        return User.builder()
                .userId(query.getUserId())
                .firstName("Rifqi")
                .lastName("Muhammad Aziz")
                .cardDetails(cardDetails)
                .build();
    }
}
