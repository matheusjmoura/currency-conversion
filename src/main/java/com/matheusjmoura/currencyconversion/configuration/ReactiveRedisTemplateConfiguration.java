package com.matheusjmoura.currencyconversion.configuration;

import com.matheusjmoura.currencyconversion.client.response.ExchangeRatesClientResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ReactiveRedisTemplateConfiguration {

    @Bean(name = "exchangeRatesRedisOperations")
    public ReactiveRedisOperations<String, ExchangeRatesClientResponse> exchangeRatesRedisOperations(
        @Qualifier("redisConnectionFactory") ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<ExchangeRatesClientResponse> valueSerializer =
            new Jackson2JsonRedisSerializer<>(ExchangeRatesClientResponse.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, ExchangeRatesClientResponse> builder =
            RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, ExchangeRatesClientResponse> context = builder
            .value(valueSerializer)
            .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
