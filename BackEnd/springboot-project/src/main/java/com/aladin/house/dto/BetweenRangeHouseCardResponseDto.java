package com.aladin.house.dto;

import java.util.List;

import com.aladin.house.vo.HouseCardVo;

import lombok.Data;

@Data
public class BetweenRangeHouseCardResponseDto {
	private int size;
	private List<HouseCardVo> houseCards;
	
	private BetweenRangeHouseCardResponseDto(int size, List<HouseCardVo> houseCards) {
		this.size = size;
		this.houseCards = houseCards;
	}
	
	public static BetweenRangeHouseCardResponseDto of(List<HouseCardVo> houseCards) {
		return new BetweenRangeHouseCardResponseDto(houseCards.size(), houseCards);
	}
}
