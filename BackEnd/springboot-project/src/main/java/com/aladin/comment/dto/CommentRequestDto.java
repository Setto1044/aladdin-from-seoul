package com.aladin.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequestDto {
	private Long roomboardsId; // 게시물 ID
	private String membersUsername; // 작성자
	private String comment; // 댓글 내용
}