package com.aladin.bookmark.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.bookmark.dto.DealBookmarkResponseDto;
import com.aladin.roomBoard.dto.RoomBoardCardDto;

@Mapper
public interface BookmarkMapper {
	int insertBoardBookmark(@Param("username") String username, @Param("roomboardsId") Long roomboardsId);

	int deleteBoardBookmark(@Param("username") String username, @Param("roomboardsId") Long roomboardsId);

	List<RoomBoardCardDto> findBoardBookmarksByCursor(@Param("username") String username, @Param("cursorId") Long cursorId, @Param("pageSize") Long pageSize);

	RoomBoardCardDto findOneBoardBookmarkById(@Param("username") String username, @Param("boardId") Long boardId);

	int insertDealBookmark(@Param("username") String username, @Param("housedealsNo") Long housedealsNo);

	int deleteDealBookmark(@Param("username") String username, @Param("housedealsNo") Long housedealsNo);

	List<DealBookmarkResponseDto> findDealBookmarksByCursor(@Param("username") String username, @Param("cursorId") Long cursorId, @Param("pageSize") Long pageSize);

	DealBookmarkResponseDto findOneDealBookmarkById(String username, Long housedealsNo);
}
