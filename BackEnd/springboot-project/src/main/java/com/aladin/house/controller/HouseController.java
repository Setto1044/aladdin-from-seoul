package com.aladin.house.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;
import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.BetweenRangeHouseCardResponseDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardResponseDto;
import com.aladin.house.dto.HouseDealWithDongCodeDto;
import com.aladin.house.dto.HouseDealWithDongCodeResponseDto;
import com.aladin.house.service.HouseService;
import com.aladin.house.vo.HouseDealStatVo;
import com.aladin.house.vo.HouseDetailVo;

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

	// 특정 아파트 시퀀스의 거래 정보 요청 커서 페이징
	@GetMapping("/deals/{aptSeq}")
	public ResponseEntity<ApiResponseDto<HouseDealWithDongCodeResponseDto>> getHouseDealsByAptSeqWithCursor(@PathVariable String aptSeq, @RequestParam(required = false) Long cursorId, @RequestParam(defaultValue = "10") int size) {
		List<HouseDealWithDongCodeDto> houseDeals = houseService.findAllHouseDealsByAptSeqWithCursor(aptSeq, cursorId, size);
		return ResponseEntity.ok(ApiResponseDto.of(true, "조회 성공", HouseDealWithDongCodeResponseDto.of(houseDeals)));
	}

	@GetMapping("/detail/{aptSeq}")
	public ResponseEntity<ApiResponseDto<HouseDetailVo>> getHouseDetail(@PathVariable String aptSeq) {
		HouseDetailVo houseDetail = houseService.getHouseDetailByAptSeq(aptSeq);
		return ResponseEntity.ok(ApiResponseDto.of(true, "아파트 상세 데이터 조회 성공", houseDetail));
	}

	@GetMapping("/stat/{aptSeq}")
	public ResponseEntity<ApiResponseDto<HouseDealStatVo>> getHouseDealStat(@PathVariable String aptSeq) {
		HouseDealStatVo stat = houseService.getHouseDealStatByAptSeq(aptSeq);
		return ResponseEntity.ok(ApiResponseDto.of(true, "거래 통계 조회 성공", stat));
	}

	// 추천 검색어 API
	@GetMapping("/search")
	public ResponseEntity<ApiResponseDto<List<String>>> searchDong(@RequestParam String keyword) {
		List<String> results = houseService.searchDong(keyword);
		return ResponseEntity.ok(ApiResponseDto.of(true, "추천 검색어 조회 성공", results));
	}
}
