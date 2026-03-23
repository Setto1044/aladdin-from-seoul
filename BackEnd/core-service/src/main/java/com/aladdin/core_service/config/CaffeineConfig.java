package com.aladdin.core_service.config;

import com.aladdin.core_service.dto.HouseSummaryNearbyResponseDto;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineConfig {
    @Bean
    public Cache<String, List<HouseSummaryNearbyResponseDto.HouseSummaryDto>> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_0000)
                .recordStats()
                .build();
    }
}
