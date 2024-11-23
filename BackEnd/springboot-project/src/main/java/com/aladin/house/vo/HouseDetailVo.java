package com.aladin.house.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HouseDetailVo {
	private String aptSeq; // 아파트 고유 번호
	private String aptName; // 아파트 이름
	private String householdCount; // 세대 수
	private String floorRange; // 층수 범위
	private String parkingCount; // 주차 공간 수
	private Integer floorAreaRatio; // 용적률
	private Integer buildingCoverageRatio; // 건폐율
	private String constructionCompany; // 건설사
	private String heatingType; // 난방 방식
	private String managementDigit; // 관리사무소 전화번호
	private String areaSizes; // 면적 크기 정보
}
