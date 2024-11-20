package com.aladin.roomBoard.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.exceptions.BoardCreationException;
import com.aladin.roomBoard.dto.BoardInsertRequestDto;
import com.aladin.roomBoard.service.BoardService;

@RestController
@RequestMapping("/boards")
public class BoardController {
	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@PostMapping
	public ResponseEntity<ApiResponseDto> insertBoard(@ModelAttribute BoardInsertRequestDto requestDto) {
		Long id = boardService.createBoard(requestDto);
		return ResponseEntity.ok(ApiResponseDto.of(true, "등록이 완료되었습니다.", id));
	}

	@ExceptionHandler(BoardCreationException.class)
	public ResponseEntity<ApiResponseDto> handleBoardCreationException(BoardCreationException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.of(false, "등록에 실패했습니다.", e.getMessage()));
	}
}
