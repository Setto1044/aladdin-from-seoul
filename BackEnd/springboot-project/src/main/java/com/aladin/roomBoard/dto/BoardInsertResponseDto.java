package com.aladin.roomBoard.dto;

public class BoardInsertResponseDto {
	private Long id;

	private BoardInsertResponseDto(Long id) {
		this.id = id;
	}

	public static BoardInsertResponseDto of(Long id) {
		return new BoardInsertResponseDto(id);
	}

}
