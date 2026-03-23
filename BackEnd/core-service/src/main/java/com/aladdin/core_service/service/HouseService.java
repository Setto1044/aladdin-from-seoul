package com.aladdin.core_service.service;

import com.aladdin.core_service.dto.HouseSummaryNearbyRequestDto;
import com.aladdin.core_service.dto.HouseSummaryNearbyResponseDto;

public interface HouseService {
    HouseSummaryNearbyResponseDto getHouseSummaryNearby(HouseSummaryNearbyRequestDto requestDto);
}
