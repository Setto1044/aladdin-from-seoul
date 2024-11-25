package com.aladin.util.subway.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubwayCongestResponseDto {
	List<Double> morningCongests;
	List<Double> eveningCongests;
	Double totalCongest;
}
