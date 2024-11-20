package com.aladin.house.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.BetweenRangeHouseCardResponseDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardResponseDto;
import com.aladin.house.dto.HouseDealResponseDto;
import com.aladin.house.service.HouseService;
import com.aladin.house.vo.HouseDealVo;

@RestController
@RequestMapping("/house")
public class HouseController {

	private final HouseService houseService;

	public HouseController(HouseService houseService) {
		this.houseService = houseService;
	}

	// 범위 내의 집 정보 요청 (Between Range)
	@GetMapping("/cards/range")
	public ResponseEntity<ApiResponseDto<BetweenRangeHouseCardResponseDto>> getHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto) {
		BetweenRangeHouseCardResponseDto responseDto = BetweenRangeHouseCardResponseDto.of(houseService.findHousesBetweenCoordinates(requestDto));
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", responseDto));
	}

	// 특정 좌표 기준으로 가까운 집 정보 요청 (Closest Coordinates)
	@GetMapping("/cards/closest")
	public ResponseEntity<ApiResponseDto<ClosestCoordinateHouseCardResponseDto>> getHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto) {
		ClosestCoordinateHouseCardResponseDto responseDto = ClosestCoordinateHouseCardResponseDto.of(houseService.findHousesClosestToCoordinates(requestDto));
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", responseDto));
	}

	// 특정 아파트 시퀀스의 거래 정보 요청
	@GetMapping("/deals/{aptSeq}")
	public ResponseEntity<ApiResponseDto<HouseDealResponseDto>> getAllHouseDealsByAptSeq(@PathVariable String aptSeq) {
		List<HouseDealVo> houseDeals = houseService.findAllHouseDealsByAptSeq(aptSeq);
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", HouseDealResponseDto.of(houseDeals)));
	}

}
