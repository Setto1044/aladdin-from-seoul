package com.aladin.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aladin.bookmark.dto.DealBookmarkResponseDto;
import com.aladin.bookmark.mapper.BookmarkMapper;
import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.roomBoard.dto.RoomBoardCardDto;
import com.aladin.viewCount.service.ViewCountService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
	private final BookmarkMapper bookmarkMapper;
	private final ViewCountService viewCountService;

	public BookmarkServiceImpl(BookmarkMapper bookmarkMapper, ViewCountService viewCountService) {
		this.bookmarkMapper = bookmarkMapper;
		this.viewCountService = viewCountService;
	}

	@Override
	public void addBoardBookmark(String username, Long roomboardsId) {
		int result = bookmarkMapper.insertBoardBookmark(username, roomboardsId);
		if (result == 0) {
			throw new RuntimeException("북마크 추가 실패: 중복되었거나 유효하지 않은 요청입니다.");
		}
	}

	@Override
	public void removeBoardBookmark(String username, Long roomboardsId) {
		int result = bookmarkMapper.deleteBoardBookmark(username, roomboardsId);
		if (result == 0) {
			throw new RuntimeException("북마크 삭제 실패: 대상이 존재하지 않거나 이미 삭제되었습니다.");
		}
	}

	@Override
	public List<RoomBoardCardDto> getBoardBookmarksByCursor(String username, Long cursorId, Long pageSize) {
		return bookmarkMapper.findBoardBookmarksByCursor(username, cursorId, pageSize);
	}

	@Override
	public RoomBoardCardDto findOneBoardBookmarkById(String username, Long boardId) {
		RoomBoardCardDto result = bookmarkMapper.findOneBoardBookmarkById(username, boardId);
		if (result == null) {
			throw new ResourceNotFoundException("존재하지 않거나 삭제된 북마크입니다.");
		}

		viewCountService.incrementRoomBoardViews(boardId);
		return result;
	}

	@Override
	public void addDealBookmark(String username, Long housedealsNo) {
		int result = bookmarkMapper.insertDealBookmark(username, housedealsNo);
		if (result == 0) {
			throw new RuntimeException("북마크 추가 실패: 중복되었거나 유효하지 않은 요청입니다.");
		}
	}

	@Override
	public void removeDealBookmark(String username, Long housedealsNo) {
		int result = bookmarkMapper.deleteDealBookmark(username, housedealsNo);
		if (result == 0) {
			throw new RuntimeException("북마크 삭제 실패: 대상이 존재하지 않거나 이미 삭제되었습니다.");
		}
	}

	@Override
	public List<DealBookmarkResponseDto> getDealBookmarksByCursor(String username, Long cursorId, Long pageSize) {
		return bookmarkMapper.findDealBookmarksByCursor(username, cursorId, pageSize);
	}

	@Override
	public DealBookmarkResponseDto findOneDealBookmarkById(String username, Long housedealsNo) {
		DealBookmarkResponseDto result = bookmarkMapper.findOneDealBookmarkById(username, housedealsNo);
		if (result == null) {
			throw new ResourceNotFoundException("존재하지 않는 거래내역입니다.");
		}
		// views 카운트 증가
		viewCountService.incrementHouseDealViews(housedealsNo);
		return result;
	}

}
