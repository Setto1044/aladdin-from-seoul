package com.aladin.bookmark.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.bookmark.dto.BookmarkDealRequestDto;
import com.aladin.bookmark.dto.BookmarkRoomRequestDto;
import com.aladin.bookmark.dto.HouseBookmarkDto;
import com.aladin.bookmark.service.BookmarkService;
import com.aladin.common.ApiResponseDto;
import com.aladin.roomBoard.dto.BoardCardDto;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
	private final BookmarkService bookmarkService;

	public BookmarkController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	@PostMapping("/board")
	public ResponseEntity<ApiResponseDto> addBoardBookmark(@RequestBody BookmarkRoomRequestDto request) {
		bookmarkService.addBoardBookmark(request.getUsername(), request.getRoomboardsId());
		return ResponseEntity.ok(ApiResponseDto.of(true, "게시글 북마크가 추가되었습니다.", null));
	}

	@DeleteMapping("/board")
	public ResponseEntity<ApiResponseDto> removeBoardBookmark(@RequestBody BookmarkRoomRequestDto request) {
		bookmarkService.removeBoardBookmark(request.getUsername(), request.getRoomboardsId());
		return ResponseEntity.ok(ApiResponseDto.of(true, "게시글 북마크가 삭제되었습니다.", null));
	}

	@GetMapping("/board/{username}")
	public ResponseEntity<ApiResponseDto<List<BoardCardDto>>> getBookmarkedBoardsByCursor(@PathVariable String username, @RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") Long pageSize) {
		List<BoardCardDto> bookmarks = bookmarkService.getBookmarkedBoardsByCursor(username, cursorId, pageSize);
		return ResponseEntity.ok(ApiResponseDto.of(true, "북마크 게시글 조회 성공", bookmarks));
	}

	@PostMapping("/deal")
	public ResponseEntity<ApiResponseDto> addDealBookmark(@RequestBody BookmarkDealRequestDto request) {
		bookmarkService.addDealBookmark(request.getUsername(), request.getHousedealsNo());
		return ResponseEntity.ok(ApiResponseDto.of(true, "거래 내역 북마크가 추가되었습니다."));
	}

	@DeleteMapping("/deal")
	public ResponseEntity<ApiResponseDto> removeDealBookmark(@RequestBody BookmarkDealRequestDto request) {
		bookmarkService.removeDealBookmark(request.getUsername(), request.getHousedealsNo());
		return ResponseEntity.ok(ApiResponseDto.of(true, "거래 내역 북마크가 삭제되었습니다."));
	}

	@GetMapping("/deal/{username}")
	public ResponseEntity<ApiResponseDto<List<HouseBookmarkDto>>> getBookmarkedHousesByCursor(@PathVariable String username, @RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") Long pageSize) {
		List<HouseBookmarkDto> bookmarks = bookmarkService.getBookmarkedHousesByCursor(username, cursorId, pageSize);
		return ResponseEntity.ok(ApiResponseDto.of(true, "북마크 매물 조회 성공", bookmarks));
	}

}
