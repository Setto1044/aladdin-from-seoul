package com.aladdin.core_service.controller;

import com.aladdin.core_service.dto.HouseSearchResultDto;
import com.aladdin.core_service.dto.HouseMapSearchRequestDto;
import com.aladdin.core_service.dto.HouseMapSearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

public interface HouseInfoApi {

    @Operation(summary = "반경 내 매물 조회", description = "위도 경도 주변 거리 반경 아파트 목록을 반환합니다.")
    HouseMapSearchResponseDto getHouseSummaryNearby(HouseMapSearchRequestDto requestDto);

    @Operation(summary = "아파트 이름 자동완성 검색", description = "입력한 키워드가 포함된 아파트 목록을 반환합니다.")
    List<HouseSearchResultDto> autocomplete(String keyword);
}
