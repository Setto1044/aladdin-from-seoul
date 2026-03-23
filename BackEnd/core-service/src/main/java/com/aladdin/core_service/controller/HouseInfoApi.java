package com.aladdin.core_service.controller;

import com.aladdin.core_service.dto.HouseSummaryNearbyRequestDto;
import com.aladdin.core_service.dto.HouseSummaryNearbyResponseDto;
import io.swagger.v3.oas.annotations.Operation;

public interface HouseInfoApi {

    @Operation(summary = "반경 내 매물 조회")
    HouseSummaryNearbyResponseDto getHouseSummaryNearby(HouseSummaryNearbyRequestDto requestDto);

}
