package com.aladin.house.dto;

import java.util.List;

import com.aladin.house.vo.HouseCardVo;

import lombok.Data;

@Data
public class ClosestCoordinateHouseCardResponseDto {
	private int size;
	private List<HouseCardVo> houseCards;
	
	private ClosestCoordinateHouseCardResponseDto(int size, List<HouseCardVo> houseCards) {
		this.size = size;
		this.houseCards = houseCards;
	}
	
	public static ClosestCoordinateHouseCardResponseDto of(List<HouseCardVo> houseCards) {
		return new ClosestCoordinateHouseCardResponseDto(houseCards.size(), houseCards);
	}
}
