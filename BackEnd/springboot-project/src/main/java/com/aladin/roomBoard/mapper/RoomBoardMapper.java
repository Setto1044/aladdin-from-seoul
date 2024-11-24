package com.aladin.roomBoard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aladin.roomBoard.dto.RoomBoardCardDto;
import com.aladin.roomBoard.dto.RoomBoardDetailDto;
import com.aladin.roomBoard.dto.RoomBoardInsertRequestDto;
import com.aladin.roomBoard.dto.RoomBoardUpdateRequestDto;
import com.aladin.roomBoard.vo.RoomBoardImageVo;

@Mapper
public interface RoomBoardMapper {
	void insertBoard(@Param("dto") RoomBoardInsertRequestDto boardInsertRequestDto);

	void insertBoardImage(Long boardId, String imageUrl, int imageOrder);

	List<RoomBoardCardDto> findBoardsByCursor(@Param("cursorId") Long cursorId, @Param("pageSize") Long pageSize);

	List<RoomBoardCardDto> findAllBoards();

	List<RoomBoardImageVo> findImagesByRoomBoardId(Long roomboardsId);

	RoomBoardDetailDto findOneByRoomBoardId(Long roomboardsId);

	void updateBoard(@Param("dto") RoomBoardUpdateRequestDto requestDto);

	void deleteImagesByRoomBoardId(Long boardId);

	int deleteBoardById(Long boardId);

	// 해시태그로 검색
	List<RoomBoardCardDto> findBoardsByHashtags(List<String> hashtags);

}
