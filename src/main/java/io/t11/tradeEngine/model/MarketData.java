package io.t11.tradeEngine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("MarketData")
public class MarketData implements Serializable {

    @Id
    private Long id;

    @Indexed
    private String ticker;

    private double Last_Traded_Price;

    private double askPrice;

    private double bidPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getLast_Traded_Price() {
        return Last_Traded_Price;
    }

    public void setLast_Traded_Price(double last_Traded_Price) {
        this.Last_Traded_Price = last_Traded_Price;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", lastTradedPrice=" + Last_Traded_Price +
                ", askPrice=" + askPrice +
                ", bidPrice=" + bidPrice +
                '}';
    }
}
