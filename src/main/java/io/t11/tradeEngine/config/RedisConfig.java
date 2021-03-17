package io.t11.tradeEngine.config;

import io.t11.tradeEngine.service.ITradeEnginePublisher;
import io.t11.tradeEngine.service.TradeEnginePublisher;
import io.t11.tradeEngine.service.ValidOrderSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    String queue(){
        return new String("orderQueue");
    }

    @Bean
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
        return new MessageListenerAdapter(new ValidOrderSubscriber(tradeObjectPublisher()),"onMessage");
    }

    @Bean
    RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    JedisPool jedisPool(){
        return new JedisPool("localhost");
    }

    //
    @Bean
    @Primary
    ITradeEnginePublisher tradeObjectPublisher(){
        ChannelTopic registersTopic=new ChannelTopic("trade-orders");
        return new TradeEnginePublisher(redisTemplate(redisConnectionFactory),jedisPool(),registersTopic,queue());
    }

}

