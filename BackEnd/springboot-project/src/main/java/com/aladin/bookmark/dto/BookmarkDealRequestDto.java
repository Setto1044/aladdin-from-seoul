package com.aladin.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkDealRequestDto {
	private String username; // 회원 username
	private Long housedealsNo; // 거래 내역 ID
}
