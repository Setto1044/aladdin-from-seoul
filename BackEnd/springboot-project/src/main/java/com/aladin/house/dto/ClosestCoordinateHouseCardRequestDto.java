package com.aladin.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClosestCoordinateHouseCardRequestDto {
	private double lat;
	private double lng;
}
