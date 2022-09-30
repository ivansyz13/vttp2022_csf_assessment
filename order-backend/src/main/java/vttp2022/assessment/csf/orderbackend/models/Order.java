package vttp2022.assessment.csf.orderbackend.models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

// IMPORTANT: You can add to this class, but you cannot delete its original content

public class Order {

	private Integer orderId;
	private String name;
	private String email;
	private Integer size;
	private String sauce;
	private Boolean thickCrust;
	private List<String> toppings = new LinkedList<>();
	private String comments;

	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public Integer getOrderId() { return this.orderId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setToppings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getToppings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

    public static Integer generateOrderId() {
        String orderId = "";

        for (int i = 0; i < 8; i++) {
            int number = (int)(Math.random() * 10);
            orderId += number;
        }

        return Integer.parseInt(orderId);
    }

    public static Order convertToOrder(String payload) {
        Order order = new Order();

        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject obj = reader.readObject();

            order.setOrderId(generateOrderId());
            order.setName(obj.getString("name"));
            order.setEmail(obj.getString("email"));
            order.setSize(obj.getInt("size"));
            order.setSauce(obj.getString("sauce"));

            String crust = obj.getString("base");
            if (crust.equalsIgnoreCase("thin"))
                order.setThickCrust(false);
            else
                order.setThickCrust(true);

            JsonArray toppingsArray = obj.getJsonArray("toppings");
            // String[] toppingsStringArray = toppingsString
            //     .substring(1, toppingsString.length() - 1)
            //     .replaceAll("\"", "")
            //     .split(",");
            List<String> toppingsList = new LinkedList<String>();
            // for (String topping : toppingsStringArray) {
            //     toppingsList.add(topping);
            // }
            for (JsonValue topping : toppingsArray) {
                String toppingStr = topping.toString().replaceAll("\"", "");
                toppingsList.add(toppingStr);
            }
            order.setToppings(toppingsList);

            order.setComments(obj.getString("comments"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

        return order;
    }
}
