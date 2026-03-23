package com.aladdin.core_service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HouseSummaryNearbyRequestDto {
    private Double latitude;
    private Double longitude;
    private Double distanceMeter;
}
