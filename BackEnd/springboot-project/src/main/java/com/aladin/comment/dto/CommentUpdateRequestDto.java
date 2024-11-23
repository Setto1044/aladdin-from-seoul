package com.aladin.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentUpdateRequestDto {
	private Long id;
	private String comment;
}
