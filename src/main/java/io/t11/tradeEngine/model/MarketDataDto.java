package io.t11.tradeEngine.model;

public class MarketDataDto {

    private String ticker;

    private int sellLimit;

    private double lastTradedPrice;

    private double maxPriceShift;

    private double askPrice;

    private double bidPrice;

    private int buyLimit;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getSellLimit() {
        return sellLimit;
    }

    public void setSellLimit(int sellLimit) {
        this.sellLimit = sellLimit;
    }

    public double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    public double getMaxPriceShift() {
        return maxPriceShift;
    }

    public void setMaxPriceShift(double maxPriceShift) {
        this.maxPriceShift = maxPriceShift;
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

    public int getBuyLimit() {
        return buyLimit;
    }

    public void setBuyLimit(int buyLimit) {
        this.buyLimit = buyLimit;
    }

    @Override
    public String toString() {
        return "MarketDataDto{" +
                "ticker='" + ticker + '\'' +
                ", sellLimit=" + sellLimit +
                ", lastTradedPrice=" + lastTradedPrice +
                ", maxPriceShift=" + maxPriceShift +
                ", askPrice=" + askPrice +
                ", bidPrice=" + bidPrice +
                ", buyLimit=" + buyLimit +
                '}';
    }
}
