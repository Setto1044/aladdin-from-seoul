package com.aladin.roomBoard.service;

import java.util.List;

import com.aladin.roomBoard.dto.RoomBoardCardDto;
import com.aladin.roomBoard.dto.RoomBoardDetailDto;
import com.aladin.roomBoard.dto.RoomBoardInsertRequestDto;
import com.aladin.roomBoard.dto.RoomBoardUpdateRequestDto;

public interface RoomBoardService {
	Long createBoard(RoomBoardInsertRequestDto requestDto);

	List<RoomBoardCardDto> findBoardsByCursor(Long cursorId, Long pageSize);

	List<RoomBoardCardDto> findAllBoards();

	RoomBoardDetailDto getBoardDetail(Long roomboardsId);

	Long updateBoard(RoomBoardUpdateRequestDto requestDto);

	int deleteBoard(Long boardId);

}
