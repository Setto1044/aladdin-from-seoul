package com.aladdin.core_service.service;

import com.aladdin.core_service.dto.HouseMapSearchRequestDto;
import com.aladdin.core_service.dto.HouseMapSearchResponseDto;

public interface HouseService {
    HouseMapSearchResponseDto getHouseSummaryNearby(HouseMapSearchRequestDto requestDto);
}
