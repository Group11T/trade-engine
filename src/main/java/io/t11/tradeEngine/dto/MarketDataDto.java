package io.t11.tradeEngine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketDataDto {

    @JsonProperty("LAST_TRADED_PRICE")
    private double lastTradedPrice;

    @JsonProperty("BID_PRICE")
    private double bidPrice;

    @JsonProperty("SELL_LIMIT")
    private String sellLimit;

    @JsonProperty("MAX_PRICE_SHIFT")
    private String maxPriceShift;

    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("ASK_PRICE")
    private double askPrice;

    @JsonProperty("BUY_LIMIT")
    private String buyLimit;

    public double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getSellLimit() {
        return sellLimit;
    }

    public void setSellLimit(String sellLimit) {
        this.sellLimit = sellLimit;
    }

    public String getMaxPriceShift() {
        return maxPriceShift;
    }

    public void setMaxPriceShift(String maxPriceShift) {
        this.maxPriceShift = maxPriceShift;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public String getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(String buyLimit) {
        this.buyLimit = buyLimit;
    }
}
