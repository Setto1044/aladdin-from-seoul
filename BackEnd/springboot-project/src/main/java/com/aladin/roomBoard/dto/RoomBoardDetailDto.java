package com.aladin.roomBoard.dto;

import java.util.List;

import com.aladin.roomBoard.vo.RoomBoardImageVo;
import com.aladin.roomBoard.vo.RoomBoardVo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomBoardDetailDto {
	private RoomBoardVo roomBoardVo;
	private List<RoomBoardImageVo> roomImageInfos;
	private String nickname;
	private String profileImagePath;
}
