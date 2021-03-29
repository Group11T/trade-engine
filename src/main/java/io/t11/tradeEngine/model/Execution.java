package io.t11.tradeEngine.model;

public class Execution {

    private String timestamp;

    private double price;

    private int quantity;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Executions{" +
                "timestamp='" + timestamp + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
