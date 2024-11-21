package com.aladin.roomBoard.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aladin.roomBoard.enums.PricePer;

import lombok.Data;

@Data
public class BoardUpdateRequestDto {
	private Long id;
	private String membersUsername; // 작성자
	private String title; // 제목
	private String detail; // 상세 내용
	private String address; // 주소
	private String postcode; // 우편번호
	private int price; // 가격
	private double houseSize; // 집 크기
	private PricePer pricePer; // 가격 단위 (month, week)
	private LocalDate rentStart; // 임대 시작 날짜
	private LocalDate rentUntil; // 임대 종료 날짜
	private List<String> hashtags; // 해시태그
	private List<MultipartFile> images; // 업로드할 이미지 리스트
}
