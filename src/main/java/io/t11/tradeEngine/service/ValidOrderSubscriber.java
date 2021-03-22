package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.dto.CreatedOrder;
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
        CreatedOrder createdOrder = null;
        try {
            createdOrder = deserializeMessageToCreatedOrder(tradeOrder);
            makeDecisionWithOrder(createdOrder);
            try {
                logger.info("Pushing order  to exchange connectivity queue: {}",  createdOrder.getId());
                orderQueuePublisher.publishOrdersToExchangeConnectivityQueue(createdOrder);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CreatedOrder deserializeMessageToCreatedOrder(Message message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        CreatedOrder createdOrder = objectMapper.readValue(message.getBody(), CreatedOrder.class);
        return createdOrder;
    }

    private void makeDecisionWithOrder(CreatedOrder createdOrder) {
    }
}
