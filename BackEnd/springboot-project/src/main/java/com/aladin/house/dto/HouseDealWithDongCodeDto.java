package com.aladin.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseDealWithDongCodeDto {
	private Long no; // 거래 고유 번호
	private String aptSeq; // 아파트 시퀀스
	private String dongCode;
	private String aptDong; // 아파트 동 이름
	private String floor; // 층수
	private Integer dealYear; // 거래 연도
	private Integer dealMonth; // 거래 월
	private Integer dealDay; // 거래 일
	private Double excluUseAr; // 전용 면적
	private String dealAmount; // 거래 금액
	private Integer views; // 조회수
}
