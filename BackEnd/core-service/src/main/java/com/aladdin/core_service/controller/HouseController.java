package com.aladdin.core_service.controller;

import com.aladdin.core_service.dto.HouseSummaryNearbyRequestDto;
import com.aladdin.core_service.dto.HouseSummaryNearbyResponseDto;
import com.aladdin.core_service.service.HouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/house")
public class HouseController implements HouseInfoApi {

    private final HouseService houseService;

    @Override
    @GetMapping("/nearby")
    public HouseSummaryNearbyResponseDto getHouseSummaryNearby(HouseSummaryNearbyRequestDto requestDto) {
        return houseService.getHouseSummaryNearby(requestDto);
    }
}
