package com.aladin.bookmark.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseBookmarkDto {
	private Long bookmarkId;
	private Long dealNo;
	private String aptSeq;
	private String dealAmount;
	private String aptName;
	private String latitude;
	private String longitude;
	private LocalDateTime bookmarkedAt;
}
