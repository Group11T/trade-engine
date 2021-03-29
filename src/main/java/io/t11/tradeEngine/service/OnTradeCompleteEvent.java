package io.t11.tradeEngine.service;

import io.t11.tradeEngine.model.MarketData;
import org.springframework.context.ApplicationEvent;

public class OnTradeCompleteEvent extends ApplicationEvent {

    private MarketData marketData;

    public OnTradeCompleteEvent(MarketData marketData){
        super(marketData);
        this.marketData = marketData;
    }

    public MarketData getMarketData() {
        return marketData;
    }
}
