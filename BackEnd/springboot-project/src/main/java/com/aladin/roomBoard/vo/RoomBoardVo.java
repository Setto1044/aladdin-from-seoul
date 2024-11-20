package com.aladin.roomBoard.vo;

import java.time.LocalDate;

import com.aladin.roomBoard.enums.PricePer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomBoardVo {
	private Long id; // 게시글 ID
	private String membersUsername; // 작성자
	private String title; // 제목
	private String detail; // 상세 내용
	private String address; // 주소
	private String postcode; // 우편번호
	private Integer price; // 가격
	private Double houseSize; // 집 크기
	private PricePer pricePer; // 가격 단위 (month 또는 week)
	private LocalDate rentStart; // 임대 시작 날짜
	private LocalDate rentUntil; // 임대 종료 날짜
	private String hashtags; // 해시태그 (JSON 문자열)
	private LocalDate createdAt; // 작성 시간
	private LocalDate updatedAt; // 수정 시간
}