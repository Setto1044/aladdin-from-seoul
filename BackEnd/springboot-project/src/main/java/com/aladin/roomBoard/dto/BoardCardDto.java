package com.aladin.roomBoard.dto;

import com.aladin.roomBoard.vo.RoomBoardVo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardCardDto {
	private RoomBoardVo roomBoardVo;
	private String thumbnailUrl;
}