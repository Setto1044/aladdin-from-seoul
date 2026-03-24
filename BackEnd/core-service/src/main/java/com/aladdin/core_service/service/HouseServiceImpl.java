package com.aladdin.core_service.service;

import com.aladdin.core_service.dto.*;
import com.aladdin.core_service.repository.HouseRepository;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final Double gridSize = 0.01; // 1Km -> 0.01;
    private static final double KR_MIN_LAT = 33.0;
    private static final double KR_MAX_LAT = 38.6;
    private static final double KR_MIN_LON = 124.5;
    private static final double KR_MAX_LON = 131.0;
    // l1 cache
    private final Cache<String, List<HouseSummaryDto>> caffeineCache;

    //l2 cache
    private final RedisTemplate<String, List<HouseSummaryDto>> redisTemplate;

    // rdb
    private final HouseRepository houseRepository;

    @Override
    public HouseMapSearchResponseDto getHouseSummaryNearby(HouseMapSearchRequestDto requestDto) {
        if (!requestDto.getScope().isDetailed()) {
            return getClusteredSummary(requestDto);
        }
        return getHouseSummary(requestDto);
    }

    private HouseMapSearchResponseDto getHouseSummary(HouseMapSearchRequestDto requestDto) {

        List<String> gridKeys = getCoveredGridKeys(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getScope().getDistanceMeter()
        );

        // L1 확인
        Map<String, List<HouseSummaryDto>> l1Hits = new HashMap<>();
        List<String> l1Misses = new ArrayList<>();

        for (String key : gridKeys) {
            List<HouseSummaryDto> l1 = caffeineCache.getIfPresent(key);
            if (l1 != null) {
                log.info("✅ L1 hit: {}", key);
                l1Hits.put(key, l1);
            } else {
                l1Misses.add(key);
            }
        }

        // L2 multiGet
        Map<String, List<HouseSummaryDto>> l2Hits = new HashMap<>();
        List<String> l2Misses = new ArrayList<>();

        if (!l1Misses.isEmpty()) {
            List<List<HouseSummaryDto>> l2Values =
                    redisTemplate.opsForValue().multiGet(l1Misses);

            for (int i = 0; i < l1Misses.size(); i++) {
                String key = l1Misses.get(i);
                List<HouseSummaryDto> val = l2Values.get(i);
                if (val != null) {
                    log.info("✅ L2 hit: {}", key);
                    l2Hits.put(key, val);
                    caffeineCache.put(key, val);
                } else {
                    l2Misses.add(key);
                }
            }
        }

        // L3 DB 조회 + pipeline으로 Redis 일괄 저장
        Map<String, List<HouseSummaryDto>> l3Results = new HashMap<>();

        for (String key : l2Misses) {
            List<HouseSummaryDto> data = loadFromDb(key);
            l3Results.put(key, data);
            caffeineCache.put(key, data);
            log.info("✅ L3 loaded: {} → {} items", key, data.size());
        }

        if (!l3Results.isEmpty()) {
            redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                l3Results.forEach((key, data) ->
                        redisTemplate.opsForValue().set(key, data, Duration.ofMinutes(30))
                );
                return null;
            });
        }

        // 결과 합산
        List<HouseSummaryDto> result = gridKeys.stream()
                .flatMap(key -> {
                    if (l1Hits.containsKey(key)) return l1Hits.get(key).stream();
                    if (l2Hits.containsKey(key)) return l2Hits.get(key).stream();
                    return l3Results.getOrDefault(key, List.of()).stream();
                })
                .distinct()
                .toList();

        return HouseMapSearchResponseDto.ofHouse(result);
    }

    private HouseMapSearchResponseDto getClusteredSummary(HouseMapSearchRequestDto requestDto) {
        boolean byDistrict = requestDto.getScope() == MapSearchScope.FAR;
        double distanceMeter = requestDto.getScope().getDistanceMeter();

        List<HouseMapClusterDto> clusters = houseRepository.getClusterCount(
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                distanceMeter,
                byDistrict
        );

        return HouseMapSearchResponseDto.ofClusters(clusters, requestDto.getScope());
    }

    private List<HouseSummaryDto> loadFromDb(String gridKey) {
        GridCenter center = parseGridCenter(gridKey);
        double half = gridSize / 2.0;

        return houseRepository.getHouseSummaryInGrid(
                        center.lat() - half, center.lat() + half,
                        center.lon() - half, center.lon() + half
                ).stream()
                .map(HouseSummaryDto::of)
                .toList();
    }

    private List<String> getCoveredGridKeys(double lat, double lon, double distanceMeter) {
        double latDelta = distanceMeter / 111_000.0;
        double lonDelta = distanceMeter / (111_000.0 * Math.cos(Math.toRadians(lat)));

        List<String> keys = new ArrayList<>();

        for (double dLat = -latDelta; dLat <= latDelta; dLat += gridSize) {
            for (double dLon = -lonDelta; dLon <= lonDelta; dLon += gridSize) {
                double gridLat = lat + dLat;
                double gridLon = lon + dLon;

                // ignore outbound of kor
                if (gridLat < KR_MIN_LAT || gridLat > KR_MAX_LAT ||
                        gridLon < KR_MIN_LON || gridLon > KR_MAX_LON) {
                    continue;
                }

                keys.add(generateGridKey(gridLat, gridLon));
            }
        }
        return keys.stream().distinct().toList();
    }

    private GridCenter parseGridCenter(String gridKey) {
        String[] parts = gridKey.split(":");
        return new GridCenter(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }

    record GridCenter(double lat, double lon) {
    }


    private String generateGridKey(double lat, double lon) {
        long latIdx = (long) Math.floor(lat / gridSize);
        long lonIdx = (long) Math.floor(lon / gridSize);

        double centerLat = (latIdx * gridSize) + (gridSize / 2.0);
        double centerLon = (lonIdx * gridSize) + (gridSize / 2.0);

        return String.format("grid:%.3f:%.3f", centerLat, centerLon);
    }
}
