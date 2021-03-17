package io.t11.tradeEngine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class TradeEnginePublisher implements ITradeEnginePublisher {

    private static Logger logger = LoggerFactory.getLogger((TradeEnginePublisher.class));

    @Autowired
    private String queue;

    private ChannelTopic topic;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    JedisPool jedisPool;

    public TradeEnginePublisher(RedisTemplate<String,Object> redisTemplate,JedisPool jedisPool, ChannelTopic topic,String queue) {
        this.topic = topic;
        this.queue = queue;
        this.redisTemplate = redisTemplate;
        this.jedisPool =jedisPool;
    }

    @Override
    public void publishTradeToRegister(Object message) throws JsonProcessingException {
        logger.info("Publishing: {}",message);
        redisTemplate.convertAndSend(topic.getTopic(),message);
    }

    @Override
    public void publishOrdersToExchangeConnectivityQueue(String tradeOrder) {
        jedisPool.getResource().lpush(queue,tradeOrder);
    }
}
