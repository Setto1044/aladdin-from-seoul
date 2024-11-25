package com.aladin.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.dto.HouseDealWithDongCodeDto;
import com.aladin.house.dto.HouseFilterRequestDto;
import com.aladin.house.vo.DongCodeVo;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.house.vo.HouseDealStatVo;
import com.aladin.house.vo.HouseDetailVo;

@Mapper
public interface HouseMapper {
	// DTO를 사용하여 범위 기반 데이터 가져오기
	List<HouseCardVo> findHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto);

	// DTO를 사용하여 특정 좌표 기준 데이터 가져오기
	List<HouseCardVo> findHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto);

	// 요청한 아파트 이름을 가지는 카드 페이징 제공
	List<HouseCardVo> findHousesByAptNameWithCursor(String keyword, String cursorId, int size);

	// 모든 housecards 조회
	List<HouseCardVo> findAllAptSeqAndAptNameOfHouseCards();

	List<HouseCardVo> findAllAptSeqAndAptNameOfHouseCardsInSeoul();

	List<HouseDealWithDongCodeDto> findAllHouseDealsByAptSeqWithCursor(String aptSeq, Long cursorId, int size);

	HouseDetailVo getHouseDetailByAptSeq(String aptSeq);

	HouseDealStatVo findHouseDealStatByAptSeq(String aptSeq);

	List<DongCodeVo> findAllDongCodes(); // 모든 동 코드를 가져오는 메서드

	List<HouseCardVo> findFilteredHouses(HouseFilterRequestDto filterRequestDto);

}
