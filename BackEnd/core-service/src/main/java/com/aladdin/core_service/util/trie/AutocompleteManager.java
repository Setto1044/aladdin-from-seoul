package com.aladdin.core_service.util.trie;

import com.aladdin.core_service.dto.HouseSearchResultDto;
import com.aladdin.core_service.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
        List<HouseSearchResultDto> allHouses = houseRepository.findAll().stream()
                .map(HouseSearchResultDto::of)
                .toList();

        int total = allHouses.size();

        for(int i = 0; i < total; i+= BATCH_SIZE) {
            int end = Math.min(i+BATCH_SIZE, total);
            List<HouseSearchResultDto> batch = allHouses.subList(i, end);
            autocompleteService.initializeDataBulk(batch);
            log.info("🚧 Batch:{} in AutocompleteService", i);
        }

        log.info("✅ AutocompleteService initialized");
    }

}
