package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.dao.MarketDataRepository;
import io.t11.tradeEngine.dao.OrderDtoRepository;
import io.t11.tradeEngine.dto.OrderDto;
import io.t11.tradeEngine.dto.OrderbookDto;
import io.t11.tradeEngine.model.ExchangeDetails;
import io.t11.tradeEngine.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class TradeDecisionService implements ITradeDecisionService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ITradeEnginePublisher tradeEnginePublisher;

    @Autowired
    MarketDataRepository marketDataRepository;

    @Autowired
    OrderDtoRepository orderDtoRepository;

    public TradeDecisionService(RestTemplate restTemplate, ITradeEnginePublisher tradeEnginePublisher) {
        this.restTemplate = restTemplate;
        this.tradeEnginePublisher = tradeEnginePublisher;
    }

    private static Logger logger = LoggerFactory.getLogger((TradeDecisionService.class));


    public OrderbookDto getMimimumAskPriceByTicker(String ticker,String exchange) throws Throwable {
        final String url = ExchangeDetails.valueOf(exchange.toUpperCase()).getUrl() + ticker + "/sell";
        ObjectMapper objectMapper = new ObjectMapper();

        List receivedOrderbook = restTemplate.getForObject(url, ArrayList.class);
        Object minimumAskPriceObject= receivedOrderbook.stream()
                .map(item-> objectMapper.convertValue(item, OrderbookDto.class))
                .filter(item->((OrderbookDto) item).getCumulatitiveQuantity() < ((OrderbookDto) item).getQuantity())
                .min(Comparator.comparing(OrderbookDto::getPrice))
                .get();

        return objectMapper.convertValue(minimumAskPriceObject, OrderbookDto.class);
    }


    public OrderbookDto getMaximumBidPriceByTicker(String ticker,String exchange) throws Throwable {
        final String url = ExchangeDetails.valueOf(exchange.toUpperCase()).getUrl() + ticker + "/buy";
        ObjectMapper objectMapper = new ObjectMapper();

        List receivedOrderbook = restTemplate.getForObject(url,ArrayList.class);
        Object maximumAskPriceObject= receivedOrderbook.stream()
                .map(item-> objectMapper.convertValue(item, OrderbookDto.class))
                .filter(item->((OrderbookDto) item).getCumulatitiveQuantity() < ((OrderbookDto) item).getQuantity())
                .max(Comparator.comparing(OrderbookDto::getPrice))
                .get();

        return objectMapper.convertValue(maximumAskPriceObject, OrderbookDto.class);
    }

    public void makeDecisionWithOrderDto(OrderDto orderDto) throws Throwable {
        Order order = null;
        double maxShiftPrice = 1.0;

        if(orderDto.getSide().equals("BUY")){
            double maxAllowedBidPrice = marketDataRepository.findByTicker(orderDto.getProduct()).get().getBidPrice() + maxShiftPrice;
            double minAllowedBidPrice = marketDataRepository.findByTicker(orderDto.getProduct()).get().getBidPrice() - maxShiftPrice;

            OrderbookDto minimumAskPriceBook1 = getMimimumAskPriceByTicker(orderDto.getProduct(),"EXCHANGE_1");
            OrderbookDto minimumAskPriceBook2 = getMimimumAskPriceByTicker(orderDto.getProduct(),"EXCHANGE_2");

            OrderbookDto maxBidBook1 =  getMaximumBidPriceByTicker(orderDto.getProduct(),"EXCHANGE_1");
            OrderbookDto maxBidBook2 = getMaximumBidPriceByTicker(orderDto.getProduct(),"EXCHANGE_2");


            int availableQuantityOnExchange1=minimumAskPriceBook1.getQuantity() - minimumAskPriceBook1.getCumulatitiveQuantity();
            int availableQuantityOnExchange2=minimumAskPriceBook2.getQuantity() - minimumAskPriceBook2.getCumulatitiveQuantity();

            double minimumAskPriceOnExchange1 = minimumAskPriceBook1.getPrice();
            double minimumAskPriceOnExchange2 = minimumAskPriceBook2.getPrice();

            if( minAllowedBidPrice < orderDto.getPrice() && orderDto.getPrice() < maxAllowedBidPrice){
                if((orderDto.getPrice() == minimumAskPriceOnExchange1 || orderDto.getPrice() == minimumAskPriceOnExchange2)
                        && (orderDto.getPrice() <= maxBidBook1.getPrice() || orderDto.getPrice() <= maxBidBook2.getPrice()) ){

                    order = makeBidOrder(orderDto,minimumAskPriceOnExchange1,minimumAskPriceOnExchange2, availableQuantityOnExchange1, availableQuantityOnExchange2);
                    try {
                        logger.info("Pushing order  to exchange connectivity queue: {}",  orderDto.getId());
                        tradeEnginePublisher.publishOrdersToExchangeConnectivityQueue(order);
                        tradeEnginePublisher.publishTradeToRecords(order);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    logger.info("Order to await for trade listener");
                    orderDtoRepository.save(orderDto);
                }

            }
            else{
                logger.info("Price too high or low");
               orderDtoRepository.save(orderDto);
            }

        }

        else if(orderDto.getSide().equals("SELL")){
            double maxAllowedAskPrice = marketDataRepository.findByTicker(orderDto.getProduct()).get().getAskPrice() + maxShiftPrice;
            double minAllowedAskPrice = marketDataRepository.findByTicker(orderDto.getProduct()).get().getAskPrice() - maxShiftPrice;

            OrderbookDto maxBidPriceBook1 = getMaximumBidPriceByTicker(orderDto.getProduct(),"EXCHANGE_1");
            OrderbookDto maxBidPriceBook2 = getMaximumBidPriceByTicker(orderDto.getProduct(),"EXCHANGE_2");

            int availableQuantityOnExchange1=maxBidPriceBook1.getQuantity() - maxBidPriceBook1.getCumulatitiveQuantity();
            int availableQuantityOnExchange2=maxBidPriceBook2.getQuantity() - maxBidPriceBook2.getCumulatitiveQuantity();

            double maximumBidPriceOnExchange1 = maxBidPriceBook1.getPrice();
            double maximumBidPriceOnExchange2 = maxBidPriceBook2.getPrice();

            if( minAllowedAskPrice < orderDto.getPrice() && orderDto.getPrice() < maxAllowedAskPrice){
                if(orderDto.getPrice() == maximumBidPriceOnExchange1 || orderDto.getPrice() == maximumBidPriceOnExchange2){
                    order = makeOfferOrder(orderDto,maximumBidPriceOnExchange1,maximumBidPriceOnExchange2, availableQuantityOnExchange1, availableQuantityOnExchange2);
                    try {
                        logger.info("Pushing order  to exchange connectivity queue: {}",  orderDto.getId());
                        tradeEnginePublisher.publishOrdersToExchangeConnectivityQueue(order);
                        tradeEnginePublisher.publishTradeToRecords(order);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    logger.info("Order to await for trade listener");
                    orderDtoRepository.save(orderDto);
                }

            }
            else{
                logger.info("Price too high or low");
                orderDtoRepository.save(orderDto);
            }

        }
    }

    public Order makeOfferOrder(OrderDto orderDto,double maximumBidPriceOnExchange1,double maximumBidPriceOnExchange2, int availableQuantityOnExchange1, int availableQuantityOnExchange2) {
        HashMap<String,Integer> tradeDetails = new HashMap<>();
        Order order = createOrder(orderDto);

        if(orderDto.getPrice() == maximumBidPriceOnExchange1){
            if(orderDto.getQuantity() <= availableQuantityOnExchange1){
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), 0);
                order.setTradeDetails(tradeDetails);
            }
            else{
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), orderDto.getQuantity() - availableQuantityOnExchange1 );
                order.setTradeDetails(tradeDetails);
            }
        }
        else if(orderDto.getPrice() == maximumBidPriceOnExchange2){
            if(orderDto.getQuantity() <= availableQuantityOnExchange2){
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), 0);
                order.setTradeDetails(tradeDetails);
            }
            else{
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), orderDto.getQuantity() - availableQuantityOnExchange2 );
                order.setTradeDetails(tradeDetails);
            }
        }
        return order;
    }

    public Order makeBidOrder(OrderDto orderDto,double minimumAskPriceOnExchange1,double minimumAskPriceOnExchange2,int availableQuantityOnExchange1,int availableQuantityOnExchange2) throws Throwable {
        HashMap<String,Integer> tradeDetails = new HashMap<>();
        Order order = createOrder(orderDto);

        if(orderDto.getPrice() == minimumAskPriceOnExchange1){
            if(orderDto.getQuantity() <= availableQuantityOnExchange1){
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), 0);
                order.setTradeDetails(tradeDetails);
            }
            else{
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), orderDto.getQuantity() - availableQuantityOnExchange1 );
                order.setTradeDetails(tradeDetails);
            }
        }
        else if(orderDto.getPrice() == minimumAskPriceOnExchange2){
            if(orderDto.getQuantity() <= availableQuantityOnExchange2){
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), 0);
                order.setTradeDetails(tradeDetails);
            }
            else{
                tradeDetails.put(ExchangeDetails.EXCHANGE_2.getExchangeName(), orderDto.getQuantity());
                tradeDetails.put(ExchangeDetails.EXCHANGE_1.getExchangeName(), orderDto.getQuantity() - availableQuantityOnExchange2 );
                order.setTradeDetails(tradeDetails);
            }
        }
        return order;
    }

    private Order createOrder(OrderDto orderDto){
        Order order = new Order();
        order.setPrice(orderDto.getPrice());
        order.setProduct(orderDto.getProduct());
        order.setSide(orderDto.getSide());
        order.setUserId(orderDto.getUserId());
        order.setOrderId(orderDto.getOrderId());
        return order;
    }

}
