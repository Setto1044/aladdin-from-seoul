package com.aladin.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BetweenRangeHouseCardRequestDto {
	private double latA;
    private double latB;
    private double lngA;
    private double lngB;
}
