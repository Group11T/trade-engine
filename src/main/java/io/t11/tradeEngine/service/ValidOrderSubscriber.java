package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.model.Order;
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
    ITradeEnginePublisher orderQueuePublisher;

    public ValidOrderSubscriber(ITradeEnginePublisher orderQueuePublisher) {
        this.orderQueuePublisher = orderQueuePublisher;
    }

    @Override
    public void onMessage(Message tradeOrder, byte[] pattern) {
        logger.info("Message Received : {}", tradeOrder);
        Order order = null;
        try {
            order = deserializeMessageToCreatedOrder(tradeOrder);
            makeDecisionWithOrder(order);
            try {
                logger.info("Pushing order  to exchange connectivity queue: {}",  order.getId());
                orderQueuePublisher.publishOrdersToExchangeConnectivityQueue(order);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Order deserializeMessageToCreatedOrder(Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        Order order = objectMapper.readValue(message.getBody(), Order.class);
        return order;
    }

    private void makeDecisionWithOrder(Order order) {
    }
}
