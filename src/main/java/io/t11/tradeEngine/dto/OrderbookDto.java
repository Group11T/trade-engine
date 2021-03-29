package io.t11.tradeEngine.dto;

import io.t11.tradeEngine.model.Execution;

import java.util.ArrayList;
import java.util.List;

public class OrderbookDto {

    private String product;

    private int quantity;

    private double price;

    private String side;

    private List<Execution> executions = new ArrayList();

    private int cumulatitiveQuantity;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> executions) {
        this.executions = executions;
    }

    public int getCumulatitiveQuantity() {
        return cumulatitiveQuantity;
    }

    public void setCumulatitiveQuantity(int cumulatitiveQuantity) {
        this.cumulatitiveQuantity = cumulatitiveQuantity;
    }

    @Override
    public String toString() {
        return "OrderbookDto{" +
                "product='" + product + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", side='" + side + '\'' +
                ", executions=" + executions +
                ", cumulatitiveQuantity='" + cumulatitiveQuantity + '\'' +
                '}';
    }
}
