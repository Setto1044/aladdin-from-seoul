package com.aladin.openData.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommercialStatusVO {
	// 기본 정보
	private String code;
	private String gugunName;
	private String businessChangeIndicator;

	// CCTV 관련
	private int cctv;
	private int cctvRank;

	// 유동인구 관련
	private double totalFloatingPopulation;
	private int floatingPopulationRank;

	// 상주인구 관련
	private int totalResidentialPopulation;
	private int residentialPopulationRank;

	// 아파트 가격 관련
	private int avgApartmentPrice;
	private int apartmentPriceRank;

	// 아파트 단지 관련
	private int avgApartmentComplexes;
	private int apartmentComplexesRank;

	// 직장인 관련
	private double avgWorkingPopulation;
	private int workingPopulationRank;
}
