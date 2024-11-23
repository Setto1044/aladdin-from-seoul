package com.aladin.roomBoard.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.roomBoard.dto.RoomBoardCardDto;
import com.aladin.roomBoard.dto.RoomBoardDetailDto;
import com.aladin.roomBoard.dto.RoomBoardInsertRequestDto;
import com.aladin.roomBoard.dto.RoomBoardUpdateRequestDto;
import com.aladin.roomBoard.service.RoomBoardService;

@RestController
@RequestMapping("/boards")
public class RoomBoardController {
	private final RoomBoardService boardService;

	public RoomBoardController(RoomBoardService boardService) {
		this.boardService = boardService;
	}

	@PostMapping
	public ResponseEntity<ApiResponseDto> insertBoard(@ModelAttribute RoomBoardInsertRequestDto requestDto) {
		System.out.println(requestDto);
		Long id = boardService.createBoard(requestDto);
		return ResponseEntity.ok(ApiResponseDto.of(true, "등록이 완료되었습니다.", id));
	}

	@GetMapping
	public ResponseEntity<ApiResponseDto<List<RoomBoardCardDto>>> getBoardsByCursor(@RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") Long pageSize) {
		List<RoomBoardCardDto> boardCards = boardService.findBoardsByCursor(cursorId, pageSize);
		if (boardCards == null || boardCards.isEmpty()) {
			throw new ResourceNotFoundException("게시물이 존재하지 않습니다.");
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", boardCards));
	}

	@GetMapping("/{roomboardsId}")
	public ResponseEntity<ApiResponseDto<RoomBoardDetailDto>> getBoardDetail(@PathVariable Long roomboardsId) {
		RoomBoardDetailDto boardDetail = boardService.getBoardDetail(roomboardsId);
		if (boardDetail == null || boardDetail.getRoomCardInfo() == null) {
			throw new ResourceNotFoundException("해당 게시물을 찾을 수 없습니다.");
		}
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", boardDetail));
	}

	@PatchMapping
	public ResponseEntity<ApiResponseDto> updateBoard(@ModelAttribute RoomBoardUpdateRequestDto requestDto) {
		Long id = boardService.updateBoard(requestDto);
		return ResponseEntity.ok(ApiResponseDto.of(true, "수정이 완료되었습니다.", id));
	}

	@DeleteMapping("/{boardId}")
	public ResponseEntity<ApiResponseDto> deleteBoard(@PathVariable Long boardId) {
		boardService.deleteBoard(boardId);
		return ResponseEntity.ok(ApiResponseDto.of(true, "삭제가 완료되었습니다.", boardId));
	}
}
