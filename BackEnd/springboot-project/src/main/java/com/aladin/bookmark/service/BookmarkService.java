package com.aladin.bookmark.service;

import java.util.List;

import com.aladin.roomBoard.dto.BoardCardDto;

public interface BookmarkService {
	void addBoardBookmark(String username, Long roomboardsId);

	void removeBoardBookmark(String username, Long roomboardsId);

	public List<BoardCardDto> getBookmarkedBoardsByCursor(String username, Long cursorId, Long pageSize);

	void addDealBookmark(String username, Long housedealsNo);

	void removeDealBookmark(String username, Long housedealsNo);
}
