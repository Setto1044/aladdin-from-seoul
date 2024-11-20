package com.aladin.house.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HouseCardVo {
	private String aptSeq;              // 아파트 고유 번호
	private String dongCode;            // 동 코드
	private String sidoName;            // 시도 이름
	private String gugunName;           // 구군 이름
	private String dongName;            // 동 이름
	private String aptName;             // 아파트 이름
	private String jibun;               // 지번
	private String latestDealAmount;    // 최신 거래 금액
	private Double excluUseAr;          // 전용 면적
	private String buildYear;           // 건축 년도
	private String latitude;            // 위도
	private String longitude;           // 경도
	private Integer views;              // 조회 수
	private String aptPhotoLink;		// 아파트 사진 링크
}
