package com.aladin.roomBoard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.roomBoard.dto.BoardInsertRequestDto;

@Mapper
public interface BoardMapper {
	void insertBoard(@Param("dto") BoardInsertRequestDto boardInsertRequestDto);

	void insertBoardImage(Long boardId, String imageUrl);
}
