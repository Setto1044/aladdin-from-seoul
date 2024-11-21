package com.aladin.roomBoard.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomBoardImageVo {
	private Long id; // 이미지 ID
	private String url; // 이미지 URL
	private Long imageOrder; // 이미지 순서
	private Long roomboardsId; // 관련 게시물 ID
}
