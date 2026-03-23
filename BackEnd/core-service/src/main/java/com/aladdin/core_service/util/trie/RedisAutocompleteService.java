package com.aladdin.core_service.util.trie;

import com.aladdin.core_service.dto.HouseSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@Primary
@RequiredArgsConstructor
public class RedisAutocompleteService implements AutocompleteService<HouseSearchResult, HouseSearchResult> {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY_PREFIX = "search:ngram:";
    private static final int N = 2;
    private static final int RESULT_COUNT = 10;

    private final Map<String, HouseSearchResult> houseMap = new HashMap<>();

    @Override
    public void initializeData(String keyword, HouseSearchResult data) {
        String seq = String.valueOf(data.getAptSeq());
        houseMap.putIfAbsent(seq, data);

        int len = keyword.length();
        if (len < N) return;

        for (int i = 0; i <= len - N; i++) {
            String gram = keyword.substring(i, i + N);
            String key = KEY_PREFIX + gram;
            redisTemplate.opsForZSet().add(key, seq, 0);
        }
    }

    @Override
    public List<HouseSearchResult> search(String keyword) {
        int len = keyword.length();

        if (len < N) {
            return Collections.emptyList();
        }

        List<String> grams = new ArrayList<>();
        for (int i = 0; i <= len - N; i++) {
            grams.add(keyword.substring(i, i + N));
        }

        String tempKey = KEY_PREFIX + "temp:" + UUID.randomUUID();

        List<String> keys = grams.stream()
                .map(g -> KEY_PREFIX + g)
                .toList();

        try {
            redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), tempKey);

            Set<String> seqs = redisTemplate.opsForZSet()
                    .reverseRange(tempKey, 0, RESULT_COUNT);

            if (seqs == null) return Collections.emptyList();

            return seqs.stream()
                    .map(houseMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } finally {
            redisTemplate.delete(tempKey);
        }
    }

    @Override
    public void initializeDataBulk(List<HouseSearchResult> dataList) {

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {

            for (HouseSearchResult house : dataList) {
                String keyword = house.getAptName();
                if (keyword == null || keyword.length() < 2) continue;

                String seq = String.valueOf(house.getAptSeq());
                houseMap.putIfAbsent(seq, house);

                int len = keyword.length();

                for (int i = 0; i <= len - 2; i++) {
                    String gram = keyword.substring(i, i + 2);
                    String key = "search:ngram:" + gram;

                    byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
                    byte[] rawValue = redisTemplate.getStringSerializer().serialize(seq);

                    connection.zSetCommands().zAdd(rawKey, 0, rawValue);
                }
            }

            return null;
        });
    }
}