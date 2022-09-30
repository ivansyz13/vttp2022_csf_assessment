package vttp2022.assessment.csf.orderbackend.services;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private PricingService priceSvc;

    @Autowired
    private OrderRepository orderRepo;

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public void createOrder(String payload) throws SQLException {
        Order newOrder = Order.convertToOrder(payload);
        boolean createOrderCheck = orderRepo.createOrder(newOrder);

        if (!createOrderCheck)
            throw new SQLException("Error - Failed to create new order");
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {
		// Use priceSvc to calculate the total cost of an order
        List<Order> orderList = orderRepo.getOrdersByEmail(email);

        if (orderList.isEmpty())
            return List.of();

        List<OrderSummary> orderSummaryList = new LinkedList<OrderSummary>();

        for (Order order : orderList) {
            OrderSummary os = new OrderSummary();
            os.setOrderId(order.getOrderId());
            os.setName(order.getName());
            os.setEmail(order.getEmail());

            Float sizePrice = priceSvc.size(order.getSize());
            Float saucePrice = priceSvc.sauce(order.getSauce());

            Float toppingsPrice = 0f;
            for (String topping : order.getToppings()) {
                toppingsPrice += priceSvc.topping(topping);
            }
            
            Float crustPrice = 0f;
            if (!order.isThickCrust())
                crustPrice = priceSvc.thinCrust();
            else
                crustPrice = priceSvc.thickCrust();

            Float totalAmount = sizePrice + saucePrice + toppingsPrice + crustPrice;
            os.setAmount(totalAmount);
            
            orderSummaryList.add(os);
        }
    
        return orderSummaryList;
	}
}
