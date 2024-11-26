package com.aladin.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.comment.dto.CommentRequestDto;
import com.aladin.comment.dto.CommentResponseDto;
import com.aladin.comment.dto.CommentUpdateRequestDto;

@Mapper
public interface CommentMapper {
	int insertComment(CommentRequestDto commentRequestDto);

	int updateComment(CommentUpdateRequestDto updateDto);

	int deleteComment(@Param("id") Long id);

	CommentResponseDto findCommentById(@Param("id") Long id);

	List<CommentResponseDto> findCommentsByRoomBoardId(@Param("roomboardsId") Long roomboardsId, @Param("cursorId") Long cursorId,
			@Param("pageSize") Long pageSize);
}
