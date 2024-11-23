package com.aladin.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.comment.dto.CommentRequestDto;
import com.aladin.comment.dto.CommentResponseDto;
import com.aladin.comment.dto.CommentUpdateRequestDto;
import com.aladin.comment.service.CommentService;
import com.aladin.common.ApiResponseDto;

@RestController
@RequestMapping("/comments")
public class CommentController {
	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping
	public ResponseEntity<ApiResponseDto> addComment(@RequestBody CommentRequestDto requestDto) {
		commentService.addComment(requestDto);
		return ResponseEntity.ok(ApiResponseDto.of(true, "댓글이 등록되었습니다."));
	}

	@PatchMapping
	public ResponseEntity<ApiResponseDto> editComment(@RequestBody CommentUpdateRequestDto requestDto) {
		commentService.editComment(requestDto);
		return ResponseEntity.ok(ApiResponseDto.of(true, "댓글이 수정되었습니다."));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long id) {
		commentService.deleteComment(id);
		return ResponseEntity.ok(ApiResponseDto.of(true, "댓글이 삭제되었습니다."));
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<ApiResponseDto<CommentResponseDto>> getCommentById(@PathVariable Long id) {
		CommentResponseDto comment = commentService.getCommentById(id);
		return ResponseEntity.ok(ApiResponseDto.of(true, "댓글 조회 성공", comment));
	}

	@GetMapping("/all/{roomboardsId}")
	public ResponseEntity<ApiResponseDto<List<CommentResponseDto>>> getComments(@PathVariable Long roomboardsId, @RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") Long pageSize) {
		List<CommentResponseDto> comments = commentService.getCommentsByRoomBoardId(roomboardsId, cursorId, pageSize);
		return ResponseEntity.ok(ApiResponseDto.of(true, "댓글 조회 성공", comments));
	}
}
