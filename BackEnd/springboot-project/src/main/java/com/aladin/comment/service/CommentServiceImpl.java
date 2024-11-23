package com.aladin.comment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aladin.comment.dto.CommentRequestDto;
import com.aladin.comment.dto.CommentResponseDto;
import com.aladin.comment.dto.CommentUpdateRequestDto;
import com.aladin.comment.mapper.CommentMapper;
import com.aladin.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	private final CommentMapper commentMapper;

	public CommentServiceImpl(CommentMapper commentMapper) {
		this.commentMapper = commentMapper;
	}

	@Override
	public void addComment(CommentRequestDto commentRequestDto) {
		int updatedRows = commentMapper.insertComment(commentRequestDto);
		if (updatedRows == 0) {
			throw new ResourceNotFoundException("댓글을 찾을 수 없습니다.");
		}
	}

	@Override
	public void editComment(CommentUpdateRequestDto updateDto) {
		int updatedRows = commentMapper.updateComment(updateDto);
		if (updatedRows == 0) {
			throw new ResourceNotFoundException("댓글을 찾을 수 없습니다.");
		}
	}

	@Override
	public void deleteComment(Long id) {
		int deletedRows = commentMapper.deleteComment(id);
		if (deletedRows == 0) {
			throw new ResourceNotFoundException("삭제할 댓글이 존재하지 않습니다.");
		}
	}

	@Override
	public CommentResponseDto getCommentById(Long id) {
		CommentResponseDto comment = commentMapper.findCommentById(id);
		if (comment == null) {
			throw new ResourceNotFoundException("해당 댓글을 찾을 수 없습니다. 댓글 ID: " + id);
		}
		return comment;
	}

	@Override
	public List<CommentResponseDto> getCommentsByRoomBoardId(Long roomboardsId, Long cursorId, Long pageSize) {
		return commentMapper.findCommentsByRoomBoardId(roomboardsId, cursorId, pageSize);
	}
}
