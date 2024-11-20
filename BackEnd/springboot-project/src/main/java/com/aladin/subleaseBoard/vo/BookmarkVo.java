package com.aladin.subleaseBoard.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkVo {
	private Long id;          // 북마크 고유 번호
	private Long memberId;    // 사용자 ID (Member 테이블 외래키)
	private Long aptCardId;   // 아파트 카드 ID (AptCards 테이블 외래키)
	private Long subleaseId;  // 전대차 게시글 ID (SubleaseBoards 테이블 외래키)
	private LocalDateTime createdAt;  // 북마크 추가 시간
}
