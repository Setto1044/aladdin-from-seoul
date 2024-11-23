package com.aladin.bookmark.service;

import java.util.List;

import com.aladin.bookmark.dto.DealBookmarkResponseDto;
import com.aladin.roomBoard.dto.RoomBoardCardDto;

public interface BookmarkService {
	void addBoardBookmark(String username, Long roomboardsId);

	void removeBoardBookmark(String username, Long roomboardsId);

	public List<RoomBoardCardDto> getBoardBookmarksByCursor(String username, Long cursorId, Long pageSize);

	RoomBoardCardDto findOneBoardBookmarkById(String username, Long boardId);

	void addDealBookmark(String username, Long housedealsNo);

	void removeDealBookmark(String username, Long housedealsNo);

	List<DealBookmarkResponseDto> getDealBookmarksByCursor(String username, Long cursorId, Long pageSize);

	DealBookmarkResponseDto findOneDealBookmarkById(String username, Long housedealsNo);

}
