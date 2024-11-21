package com.aladin.roomBoard.service;

import java.util.List;

import com.aladin.roomBoard.dto.BoardCardDto;
import com.aladin.roomBoard.dto.BoardDetailDto;
import com.aladin.roomBoard.dto.BoardInsertRequestDto;
import com.aladin.roomBoard.dto.BoardUpdateRequestDto;

public interface BoardService {
	Long createBoard(BoardInsertRequestDto requestDto);

	List<BoardCardDto> findBoardsByCursor(Long cursorId, Long pageSize);

	BoardDetailDto getBoardDetail(Long roomboardsId);

	Long updateBoard(BoardUpdateRequestDto requestDto);

	int deleteBoard(Long boardId);

}
