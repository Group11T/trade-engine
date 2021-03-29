package io.t11.tradeEngine.config;

import io.t11.tradeEngine.dao.MarketDataRepository;
import io.t11.tradeEngine.model.Order;
import io.t11.tradeEngine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPool;

@Configuration
@EnableRedisRepositories(basePackages = "io.t11.tradeEngine.dao")
public class RedisConfig {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    ChannelTopic ordersTopic() {
        return new ChannelTopic("valid-orders");
    }

    @Bean
    RedisMessageListenerContainer messageListenerContainer(){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter(), ordersTopic());
        return redisMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(){
        return new MessageListenerAdapter(new ValidOrderSubscriber(tradeService()),"onMessage");
    }

    @Bean
    RedisTemplate<String, Order> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Order> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    JedisPool jedisPool(){
        return new JedisPool("redis-18040.c257.us-east-1-3.ec2.cloud.redislabs.com",18040, "default","TGYqAObAPjsrZEd5KbDnzBexK5MYWTBS");
    }

    //

    ITradeEnginePublisher tradeObjectPublisher(){
        return new TradeEnginePublisher(redisTemplate(redisConnectionFactory),jedisPool());
    }


    @Bean
    @Primary
    ITradeDecisionService tradeService(){
        return new TradeDecisionService(restTemplate(),tradeObjectPublisher());
    }

}

