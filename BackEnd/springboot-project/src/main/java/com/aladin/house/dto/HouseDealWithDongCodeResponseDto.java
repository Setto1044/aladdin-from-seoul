package com.aladin.house.dto;

import java.util.List;

import lombok.Data;

@Data
public class HouseDealWithDongCodeResponseDto {
	private int size;
	private List<HouseDealWithDongCodeDto> housedeals;

	private HouseDealWithDongCodeResponseDto(int size, List<HouseDealWithDongCodeDto> housedeals) {
		this.size = size;
		this.housedeals = housedeals;
	}

	public static HouseDealWithDongCodeResponseDto of(List<HouseDealWithDongCodeDto> housedeals) {
		return new HouseDealWithDongCodeResponseDto(housedeals.size(), housedeals);
	}
}
