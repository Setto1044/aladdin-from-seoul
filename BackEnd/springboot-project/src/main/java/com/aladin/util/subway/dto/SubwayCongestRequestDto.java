package com.aladin.util.subway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubwayCongestRequestDto {
	private String[] stationNames;
}
