package com.aladin.house.service;

import java.util.List;

import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.dto.HouseDealWithDongCodeDto;
import com.aladin.house.dto.HouseFilterRequestDto;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.house.vo.HouseDealStatVo;
import com.aladin.house.vo.HouseDealVo;
import com.aladin.house.vo.HouseDetailVo;

public interface HouseService {
	// 특정 범위의 데이터 가져오기
	List<HouseCardVo> findHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto);

	// 특정 위도, 경도를 기준으로 가까운 데이터 가져오기
	List<HouseCardVo> findHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto);

	// 요청한 아파트 이름을 가지는 카드 마커정보 제공
	List<HouseCardVo> findHousesByAptNameWithCursor(String keyword, String cursorId, int size);

	// 아파트 고유 번호 -> 아파트 거래 상세 정보 리스트 가져오기
	List<HouseDealWithDongCodeDto> findAllHouseDealsByAptSeqWithCursor(String aptSeq, Long cursorId, int size);

	HouseDetailVo getHouseDetailByAptSeq(String aptSeq);

	HouseDealStatVo getHouseDealStatByAptSeq(String aptSeq);

	List<HouseCardVo> findFilteredHouses(HouseFilterRequestDto filterRequestDto);

	List<HouseDealVo> findTop3ViewsHouseDeal();
}