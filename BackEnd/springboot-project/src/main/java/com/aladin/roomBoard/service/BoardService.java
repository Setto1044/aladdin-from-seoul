package com.aladin.roomBoard.service;

import com.aladin.roomBoard.dto.BoardInsertRequestDto;

public interface BoardService {
	public Long createBoard(BoardInsertRequestDto requestDto);
}
