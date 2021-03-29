package io.t11.tradeEngine.service;

import io.t11.tradeEngine.dto.OrderDto;
import io.t11.tradeEngine.dto.OrderbookDto;

public interface ITradeDecisionService {

    OrderbookDto getMimimumAskPriceByTicker(String ticker,String exchange) throws Throwable;

    OrderbookDto getMaximumBidPriceByTicker(String ticker, String exchange) throws Throwable;

    void makeDecisionWithOrderDto(OrderDto orderDto) throws Throwable;
}
