package com.aladin.comment.service;

import java.util.List;

import com.aladin.comment.dto.CommentRequestDto;
import com.aladin.comment.dto.CommentResponseDto;
import com.aladin.comment.dto.CommentUpdateRequestDto;

public interface CommentService {
	void addComment(CommentRequestDto commentRequestDto);

	void editComment(CommentUpdateRequestDto updateDto);

	void deleteComment(Long id);

	CommentResponseDto getCommentById(Long id);

	List<CommentResponseDto> getCommentsByRoomBoardId(Long roomboardsId, Long cursorId, Long pageSize);
}
