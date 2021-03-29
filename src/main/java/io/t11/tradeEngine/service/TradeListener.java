package io.t11.tradeEngine.service;

import io.t11.tradeEngine.dao.OrderDtoRepository;
import io.t11.tradeEngine.dto.OrderDto;
import io.t11.tradeEngine.model.MarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.List;
import java.util.stream.Collectors;

public class TradeListener  implements ApplicationListener<OnTradeCompleteEvent> {

    @Autowired
    OrderDtoRepository orderDtoRepository;

    @Autowired
    TradeDecisionService tradeDecisionService;

    @Override
    public void onApplicationEvent(OnTradeCompleteEvent event) {
        this.fetchOrdersAndPublish(event);
    }

    private void fetchOrdersAndPublish(final OnTradeCompleteEvent event) {
        MarketData marketData = event.getMarketData();
        List<OrderDto> sellOrders = orderDtoRepository.findByPrice(marketData.getAskPrice());
        sellOrders.stream()
                .filter(item-> item.getSide().equals("SELL"))
                .map(orderItem-> {
                    try {
                          tradeDecisionService.makeDecisionWithOrderDto(orderItem);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return orderItem;
                }).collect(Collectors.toList());

        List<OrderDto> buyOrders = orderDtoRepository.findByPrice(marketData.getBidPrice());
        buyOrders.stream()
                .filter(item-> item.getSide().equals("BUY"))
                .map(orderItem-> {
                    try {
                        tradeDecisionService.makeDecisionWithOrderDto(orderItem);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return orderItem;
                }).collect(Collectors.toList());
    }
}
