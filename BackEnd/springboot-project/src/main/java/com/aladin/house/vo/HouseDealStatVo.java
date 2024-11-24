package com.aladin.house.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseDealStatVo {
	private String aptSeq; // 아파트 고유 번호
	private String dealDateOldest; // 가장 오래된 거래 날짜
	private String dealDateLatest; // 가장 최근 거래 날짜
	private Double excluUseArMin; // 가장 작은 크기
	private Double excluUseArMax; // 가장 큰 크기
	private String dealAmountMin; // 가장 작은 거래 금액
	private String dealAmountMax; // 가장 큰 거래 금액
}
