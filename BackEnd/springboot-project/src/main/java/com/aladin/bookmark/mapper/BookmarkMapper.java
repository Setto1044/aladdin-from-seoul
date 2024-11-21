package com.aladin.bookmark.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.roomBoard.dto.BoardCardDto;

@Mapper
public interface BookmarkMapper {
	void insertBoardBookmark(@Param("username") String username, @Param("roomboardsId") Long roomboardsId);

	void deleteBoardBookmark(@Param("username") String username, @Param("roomboardsId") Long roomboardsId);

	List<BoardCardDto> findBookmarkedBoardsByCursor(@Param("username") String username, @Param("cursorId") Long cursorId, @Param("pageSize") Long pageSize);
}
