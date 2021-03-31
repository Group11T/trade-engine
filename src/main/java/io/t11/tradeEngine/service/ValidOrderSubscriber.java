package io.t11.tradeEngine.service;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ValidOrderSubscriber implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger((ValidOrderSubscriber.class));

    @Autowired
    ITradeDecisionService tradeDecisionService;

    public ValidOrderSubscriber(ITradeDecisionService tradeDecisionService) {
        this.tradeDecisionService = tradeDecisionService;
    }

    @Override
    public void onMessage(Message tradeOrder, byte[] pattern) {
        logger.info("Message Received : {}", tradeOrder);
        try {
           OrderDto orderDto = deserializeMessageToOrderDto(tradeOrder);
           tradeDecisionService.makeDecisionWithOrderDto(orderDto);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public OrderDto deserializeMessageToOrderDto(Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        OrderDto orderDto = objectMapper.readValue(message.getBody(), OrderDto.class);
        System.out.println(orderDto);
        return orderDto;
    }

}
