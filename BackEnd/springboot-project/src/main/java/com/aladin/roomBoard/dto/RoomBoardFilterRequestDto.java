package com.aladin.roomBoard.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomBoardFilterRequestDto {
	private List<String> hashtags;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate rentFrom;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate rentTo;
	private String address;
	private Integer priceMin;
	private Integer priceMax;
	private Double sizeMin;
	private Double sizeMax;
}
