package com.aladin.roomBoard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.roomBoard.dto.BoardCardDto;
import com.aladin.roomBoard.dto.BoardInsertRequestDto;

@Mapper
public interface BoardMapper {
	void insertBoard(@Param("dto") BoardInsertRequestDto boardInsertRequestDto);

	void insertBoardImage(Long boardId, String imageUrl, int imageOrder);

	List<BoardCardDto> findBoardsByCursor(@Param("cursorId") Long cursorId, @Param("pageSize") Long pageSize);
}
