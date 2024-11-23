package com.aladin.comment.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponseDto {
	private Long id; // 댓글 ID
	private Long roomboardsId; // 게시물 ID
	private String membersUsername; // 작성자
	private String comment; // 댓글 내용
	private LocalDateTime createdAt; // 작성 시간
	private LocalDateTime modifiedAt; // 수정 시간
}