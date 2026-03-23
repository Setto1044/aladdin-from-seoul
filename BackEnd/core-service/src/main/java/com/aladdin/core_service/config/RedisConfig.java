package com.aladdin.core_service.config;

import com.aladdin.core_service.dto.HouseSummaryNearbyResponseDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, List<HouseSummaryNearbyResponseDto.HouseSummaryDto>> houseRedisTemplate(
            RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {

        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, HouseSummaryNearbyResponseDto.HouseSummaryDto.class);

        Jackson2JsonRedisSerializer<List<HouseSummaryNearbyResponseDto.HouseSummaryDto>> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, type);

        RedisTemplate<String, List<HouseSummaryNearbyResponseDto.HouseSummaryDto>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        return template;
    }
}
