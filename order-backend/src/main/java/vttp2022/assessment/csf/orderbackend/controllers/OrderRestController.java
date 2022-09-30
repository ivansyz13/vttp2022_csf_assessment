package vttp2022.assessment.csf.orderbackend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class OrderRestController {
    @Autowired private OrderService orderSvc;
    
    @PostMapping(path="/order", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postOrder(@RequestBody String payload) {
        JsonObject resp;

        try {
            orderSvc.createOrder(payload);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            resp = Json.createObjectBuilder().add("Error", ex.getMessage()).build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp.toString());
        }

        resp = Json.createObjectBuilder().add("Order created", payload).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp.toString());
    }

    @GetMapping(path="/order/{email}/all")
    public ResponseEntity<String> getAllOrders(@PathVariable String email) {  
        List<OrderSummary> orderSummaryList = orderSvc.getOrdersByEmail(email);

        if (orderSummaryList.isEmpty()) {
            JsonObject error = Json.createObjectBuilder().add("Error", "No orders found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error.toString());
        }

        JsonArrayBuilder respBuilder = Json.createArrayBuilder();
        for (OrderSummary os : orderSummaryList) {
            JsonObject data = Json.createObjectBuilder()
                .add("order_id", os.getOrderId())
                .add("name", os.getName())
                .add("email", os.getEmail())
                .add("amount", os.getAmount())
                .build();
            respBuilder.add(data);
        }

        return ResponseEntity.ok(respBuilder.build().toString());
    }
}
