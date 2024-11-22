package com.aladin.roomBoard.dto;

public class RoomBoardInsertResponseDto {
	private Long id;

	private RoomBoardInsertResponseDto(Long id) {
		this.id = id;
	}

	public static RoomBoardInsertResponseDto of(Long id) {
		return new RoomBoardInsertResponseDto(id);
	}

}
