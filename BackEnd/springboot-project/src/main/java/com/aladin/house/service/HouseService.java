package com.aladin.house.service;

import java.util.List;

import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.house.vo.HouseDealStatVo;
import com.aladin.house.vo.HouseDealVo;
import com.aladin.house.vo.HouseDetailVo;

public interface HouseService {
	// 특정 범위의 데이터 가져오기
	List<HouseCardVo> findHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto);

	// 특정 위도, 경도를 기준으로 가까운 데이터 가져오기
	List<HouseCardVo> findHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto);

	// 아파트 고유 번호 -> 아파트 거래 상세 정보 리스트 가져오기
	List<HouseDealVo> findAllHouseDealsByAptSeqWithCursor(String aptSeq, Long cursorId, int size);

	HouseDetailVo getHouseDetailByAptSeq(String aptSeq);

	HouseDealStatVo getHouseDealStatByAptSeq(String aptSeq);
}