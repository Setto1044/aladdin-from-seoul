package com.aladdin.core_service.util.trie;

import com.aladdin.core_service.dto.HouseSearchResult;
import com.aladdin.core_service.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutocompleteManager {

    private final HouseRepository houseRepository;
    private final AutocompleteService autocompleteService;
    private static final int BATCH_SIZE = 1000;

    //@EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("🚧 initializing AutocompleteService");
        List<HouseSearchResult> allHouses = houseRepository.findAll().stream()
                .map(HouseSearchResult::of)
                .toList();

        int total = allHouses.size();

        for(int i = 0; i < total; i+= BATCH_SIZE) {
            int end = Math.min(i+BATCH_SIZE, total);
            List<HouseSearchResult> batch = allHouses.subList(i, end);
            autocompleteService.initializeDataBulk(batch);
            log.info("🚧 Batch:{} in AutocompleteService", i);
        }

        log.info("✅ AutocompleteService initialized");
    }

}
