package io.t11.tradeEngine.model;

public enum ExchangeDetails {

    EXCHANGE_1("exchange_1","https://exchange.matraining.com/orderbook/"),
    EXCHANGE_2("exchange_2","https://exchange2.matraining.com/orderbook/");

    private final String exchangeName;
    private final String url;

    ExchangeDetails(String exchangeName,String url) {
        this.exchangeName = exchangeName;
        this.url = url;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ExchangeDetails{" +
                "exchangeName='" + exchangeName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

