package com.aladin.house.dto;

import java.util.List;

import com.aladin.house.vo.HouseDealVo;

import lombok.Data;

@Data
public class HouseDealResponseDto {
	private int size;
	private List<HouseDealVo> housedeals;

	private HouseDealResponseDto(int size, List<HouseDealVo> housedeals) {
		this.size = size;
		this.housedeals = housedeals;
	}

	public static HouseDealResponseDto of(List<HouseDealVo> housedeals) {
		return new HouseDealResponseDto(housedeals.size(), housedeals);
	}
}
