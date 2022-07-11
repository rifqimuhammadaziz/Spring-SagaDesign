package rifqimuhammadaziz.orderservice.command.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rifqimuhammadaziz.orderservice.command.api.model.OrderRestModel;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {
        return "Order created";
    }
}
