package vttp2022.assessment.csf.orderbackend.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;

@Repository
public class OrderRepository {
    @Autowired private JdbcTemplate template;

    public boolean createOrder(Order newOrder) {
        try {
            template.update(
                "insert into orders values (?, ?, ?, ?, ?, ?, ?, ?)", 
                newOrder.getOrderId(), newOrder.getName(),
                newOrder.getEmail(), newOrder.getSize(), 
                newOrder.isThickCrust(), newOrder.getSauce(), 
                newOrder.getToppings().toString(), newOrder.getComments()
            );
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public List<Order> getOrdersByEmail(String email) {
        SqlRowSet result = template.queryForRowSet(
            "select * from orders where email = ?", email
        );

        if (!result.next())
            return List.of();
        else
            result.beforeFirst();

        List<Order> orderList = new LinkedList<Order>();
        while (result.next()) {
            Order order = new Order();
            order.setOrderId(result.getInt("order_id"));
            order.setName(result.getString("name"));
            order.setEmail(result.getString("email"));
            order.setSize(result.getInt("pizza_size"));
            order.setSauce(result.getString("sauce"));
            order.setThickCrust(result.getBoolean("thick_crust"));

            String toppingsStr = result.getString("toppings");
            String[] toppingsStrArray = toppingsStr.substring(1, toppingsStr.length() - 1).split(",");
            List<String> toppingsList = new LinkedList<String>();
            for (String topping : toppingsStrArray) {
                toppingsList.add(topping);
            }
            order.setToppings(toppingsList);
            
            order.setComments(result.getString("comments"));

            orderList.add(order);
        }

        return orderList;
    }
}
