package com.aladin.subleaseBoard.vo;

import java.time.LocalDate;
import java.util.List;

import com.aladin.subleaseBoard.PricePer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubleaseBoardVo {
	private Long id;                 // 기본키
	private String title;            // 매물 간단한 제목
	private String detail;           // 상세 설명
	private String address;          // 매물 주소
	private Integer price;           // 가격
	private Double houseSize;        // 방 사이즈
	private PricePer pricePer;       // 주당-월당 가격 (enum -> week, month)
	private String latitude;         // 위도
	private String longitude;        // 경도
	private LocalDate rentUntil;     // 임대 종료 날짜
	private List<String> options;    // 매물 옵션 해시태그
	private Integer views;           // 조회 수
	private Long hostId;             // 게시글 작성자 id 외래키
	private List<String> imageUrls;  // 첨부 이미지 URL 목록
	private List<CommentVo> comments;  // 댓글 목록
}
