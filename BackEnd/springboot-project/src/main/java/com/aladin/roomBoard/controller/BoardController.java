package com.aladin.roomBoard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.roomBoard.dto.BoardCardDto;
import com.aladin.roomBoard.dto.BoardDetailDto;
import com.aladin.roomBoard.dto.BoardInsertRequestDto;
import com.aladin.roomBoard.dto.BoardUpdateRequestDto;
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

	@GetMapping
	public ResponseEntity<ApiResponseDto<List<BoardCardDto>>> getBoardsByCursor(@RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") Long pageSize) {
		List<BoardCardDto> boardCards = boardService.findBoardsByCursor(cursorId, pageSize);
		if (boardCards == null || boardCards.isEmpty()) {
			throw new ResourceNotFoundException("게시물이 존재하지 않습니다.");
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", boardCards));
	}

	@GetMapping("/{roomboardsId}")
	public ResponseEntity<ApiResponseDto<BoardDetailDto>> getBoardDetail(@PathVariable Long roomboardsId) {
		BoardDetailDto boardDetail = boardService.getBoardDetail(roomboardsId);
		if (boardDetail == null || boardDetail.getRoomCardInfo() == null) {
			throw new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다.");
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", boardDetail));
	}

	@PutMapping
	public ResponseEntity<ApiResponseDto> updateBoard(@ModelAttribute BoardUpdateRequestDto requestDto) {
		Long id = boardService.updateBoard(requestDto);
		return ResponseEntity.ok(ApiResponseDto.of(true, "수정이 완료되었습니다.", id));
	}
}
