package com.aladin.util.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.util.subway.dto.SubwayCongestRequestDto;
import com.aladin.util.subway.dto.SubwayCongestResponseDto;
import com.aladin.util.subway.service.SubwayService;

@RestController
@RequestMapping("/subway")
public class SubwayController {

	private final SubwayService subwayService;

	public SubwayController(SubwayService subwayService) {
		super();
		this.subwayService = subwayService;
	}

	@PostMapping
	public ResponseEntity<ApiResponseDto<SubwayCongestResponseDto>> getSubwayPathCongests(@RequestBody SubwayCongestRequestDto request) {
		SubwayCongestResponseDto result = subwayService.calculateTrafficJam(request.getStationNames());
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", result));
	}
}
