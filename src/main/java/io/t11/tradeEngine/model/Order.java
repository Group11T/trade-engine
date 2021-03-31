package io.t11.tradeEngine.model;

import java.util.HashMap;
import java.util.Map;


public class Order {

    private Long orderId;

    private String product;

    private double price;

    private String side;

    private Long userId;

    /*Map of each exchange and the quantity to trade,
     *String is name of exchange to trade,Integer is number of stocks to trade
     */
    private Map<String,Integer> tradeDetails = new HashMap<>();

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<String, Integer> getTradeDetails() {
        return tradeDetails;
    }

    public void setTradeDetails(Map<String, Integer> tradeDetails) {
        this.tradeDetails = tradeDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", product='" + product + '\'' +
                ", price=" + price +
                ", side='" + side + '\'' +
                ", userId=" + userId +
                ", tradeDetails=" + tradeDetails +
                '}';
    }
}
