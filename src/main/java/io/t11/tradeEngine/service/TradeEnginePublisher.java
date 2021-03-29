package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.t11.tradeEngine.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

@Component
public class TradeEnginePublisher implements ITradeEnginePublisher {

    private static Logger logger = LoggerFactory.getLogger((TradeEnginePublisher.class));

    String queue(){
        return new String("orderQueue");
    }

    private ChannelTopic registerTopic(){
        return new ChannelTopic("tradeService");
    };

    @Autowired
    RedisTemplate<String,Order> redisTemplate;

    @Autowired
    JedisPool jedisPool;

    public TradeEnginePublisher(RedisTemplate<String,Order> redisTemplate,JedisPool jedisPool) {
        this.redisTemplate = redisTemplate;
        this.jedisPool =jedisPool;
    }

    @Override
    public void publishOrdersToExchangeConnectivityQueue(Order order) throws JsonProcessingException {
        logger.info("Publishing: {}", order," to exchange connectivity");
        ObjectMapper objectMapper = new ObjectMapper();
        jedisPool.getResource().lpush(queue(), objectMapper.writeValueAsString(order));
    }

    @Override
    public void publishTradeToRecords(Order order) throws JsonProcessingException {
        logger.info("Publishing: {}", order," to register");
        redisTemplate.convertAndSend(registerTopic().getTopic(), order);
    }
}
