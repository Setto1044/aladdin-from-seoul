package com.aladin.subleaseBoard.vo;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentVo {
	private Long id;                // 댓글 고유 번호
	private Long postId;            // 해당 게시물 ID
	private Long userId;            // 댓글 작성자 ID
	private String username;        // 댓글 작성자 이름
	private String content;         // 댓글 내용
	private LocalDateTime createdAt; // 댓글 작성 시간
}
