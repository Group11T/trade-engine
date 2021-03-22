package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.t11.tradeEngine.dto.CreatedOrder;
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
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    JedisPool jedisPool;

    public TradeEnginePublisher(RedisTemplate<String,Object> redisTemplate,JedisPool jedisPool) {
        this.redisTemplate = redisTemplate;
        this.jedisPool =jedisPool;
    }

    @Override
    public void publishTradeToRegister(CreatedOrder createdOrder) throws JsonProcessingException {
        logger.info("Publishing: {}", createdOrder," to register");
        redisTemplate.convertAndSend(registerTopic().getTopic(), createdOrder);
    }

    @Override
    public void publishOrdersToExchangeConnectivityQueue(CreatedOrder createdOrder) throws JsonProcessingException {
        logger.info("Publishing: {}", createdOrder," to exchange connectivity");
        jedisPool.getResource().lpush(queue(), createdOrder.toString());
    }
}
