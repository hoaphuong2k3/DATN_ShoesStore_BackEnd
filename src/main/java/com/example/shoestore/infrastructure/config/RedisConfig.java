package com.example.shoestore.infrastructure.config;

import com.example.shoestore.core.sale.cart.model.entity.ShoesCart;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Long, ShoesCart> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, ShoesCart> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new GenericToStringSerializer<>(Long.class));

        Jackson2JsonRedisSerializer<ShoesCart> jsonSerializer = new Jackson2JsonRedisSerializer<>(ShoesCart.class);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        return template;
    }
}
