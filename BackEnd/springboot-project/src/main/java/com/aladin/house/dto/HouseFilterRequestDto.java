package com.aladin.house.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseFilterRequestDto {
	private Double excluUseArMin;
	private Double excluUseArMax;
	private String dealAmountMin;
	private String dealAmountMax;
	private String buildYearMin;
	private String buildYearMax;
	private Double latA; // 위도 최소값
	private Double latB; // 위도 최대값
	private Double lngA; // 경도 최소값
	private Double lngB; // 경도 최대값
}