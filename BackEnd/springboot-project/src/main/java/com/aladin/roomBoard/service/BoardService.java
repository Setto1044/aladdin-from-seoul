package com.aladin.roomBoard.service;

import java.util.List;

import com.aladin.roomBoard.dto.BoardCardDto;
import com.aladin.roomBoard.dto.BoardInsertRequestDto;

public interface BoardService {
	public Long createBoard(BoardInsertRequestDto requestDto);

	List<BoardCardDto> findBoardsByCursor(Long cursorId, Long pageSize);

}
