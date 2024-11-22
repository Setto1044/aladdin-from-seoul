package com.aladin.bookmark.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aladin.bookmark.dto.HouseBookmarkDto;
import com.aladin.bookmark.mapper.BookmarkMapper;
import com.aladin.roomBoard.dto.BoardCardDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {
	private final BookmarkMapper bookmarkMapper;

	public BookmarkServiceImpl(BookmarkMapper bookmarkMapper) {
		this.bookmarkMapper = bookmarkMapper;
	}

	@Override
	public void addBoardBookmark(String username, Long roomboardsId) {
		try {
			bookmarkMapper.insertBoardBookmark(username, roomboardsId);
		} catch (Exception e) {
			log.error("게시글 북마크 추가 중 오류: {}", e.getMessage(), e);
			throw new RuntimeException("게시글 북마크 추가 중 오류가 발생했습니다.");
		}
	}

	@Override
	public void removeBoardBookmark(String username, Long roomboardsId) {
		try {
			bookmarkMapper.deleteBoardBookmark(username, roomboardsId);
		} catch (Exception e) {
			log.error("게시글 북마크 삭제 중 오류: {}", e.getMessage(), e);
			throw new RuntimeException("게시글 북마크 삭제 중 오류가 발생했습니다.");
		}
	}

	@Override
	public List<BoardCardDto> getBookmarkedBoardsByCursor(String username, Long cursorId, Long pageSize) {
		return bookmarkMapper.findBookmarkedBoardsByCursor(username, cursorId, pageSize);
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
	public List<HouseBookmarkDto> getBookmarkedHousesByCursor(String username, Long cursorId, Long pageSize) {
		return bookmarkMapper.findBookmarkedDealsByCursor(username, cursorId, pageSize);
	}

}
