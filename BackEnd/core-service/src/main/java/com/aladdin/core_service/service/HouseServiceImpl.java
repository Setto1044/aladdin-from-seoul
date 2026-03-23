package com.aladdin.core_service.service;

import com.aladdin.core_service.dto.HouseSummaryNearbyRequestDto;
import com.aladdin.core_service.dto.HouseSummaryNearbyResponseDto;
import com.aladdin.core_service.entity.HouseInfo;
import com.aladdin.core_service.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;

    @Override
    public HouseSummaryNearbyResponseDto getHouseSummaryNearby(HouseSummaryNearbyRequestDto requestDto) {
        List<HouseInfo> houses = houseRepository.getHouseSummaryNearby(requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getDistanceMeter());

        List<HouseSummaryNearbyResponseDto.HouseSummaryDto> summaryDtoList = houses.stream()
                .map(HouseSummaryNearbyResponseDto.HouseSummaryDto::of)
                .toList();

        return HouseSummaryNearbyResponseDto.of(summaryDtoList);
    }
}
