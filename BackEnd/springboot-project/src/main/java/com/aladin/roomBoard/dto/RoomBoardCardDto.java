package com.aladin.roomBoard.dto;

import com.aladin.roomBoard.vo.RoomBoardVo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomBoardCardDto {
	private RoomBoardVo roomBoardVo;
	private String thumbnailUrl;
	private String nickname;
	private String profileImagePath;
}