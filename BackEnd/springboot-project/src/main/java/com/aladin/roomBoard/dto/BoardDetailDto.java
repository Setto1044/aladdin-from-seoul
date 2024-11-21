package com.aladin.roomBoard.dto;

import java.util.List;

import com.aladin.roomBoard.vo.RoomBoardImageVo;
import com.aladin.roomBoard.vo.RoomBoardVo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDetailDto {
	private RoomBoardVo roomCardInfo;
	private List<RoomBoardImageVo> roomImageInfos;
}
