package com.aladin.bookmark.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DealBookmarkResponseDto {
	// bookmark
	private Long bookmarkId;
	private Long dealNo;
	private String aptSeq;
	private String dealAmount;
	private String aptName;
	private String latitude;
	private String longitude;
	// card
	private String sidoName;
	private String gugunName;
	private String dongName;
	private String jibun;
	private String buildYear;
	// deals
	private Double excluUseAr;
	private String floor;
	private String aptDong;
	private Integer dealYear;
	private Integer dealMonth;
	private Integer dealDay;
	private LocalDateTime bookmarkedAt;
	private Integer views;
}
