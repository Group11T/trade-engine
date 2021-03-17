package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        logger.info("Message Received: {}", tradeOrder.toString());
        makeDecisionWithOrder(tradeOrder);
        try {
            logger.info("Pushing Received to exchange connectivity: {}",  tradeOrder);
            orderQueuePublisher.publishOrdersToExchangeConnectivityQueue(tradeOrder.getBody().toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void makeDecisionWithOrder(Message message) {
    }
}
