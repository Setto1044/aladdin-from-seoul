package com.aladin.house.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.mapper.HouseMapper;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.house.vo.HouseDealStatVo;
import com.aladin.house.vo.HouseDealVo;
import com.aladin.house.vo.HouseDetailVo;

@Service
public class HouseServiceImpl implements HouseService {
	private final HouseMapper houseMapper;

	public HouseServiceImpl(HouseMapper houseMapper) {
		super();
		this.houseMapper = houseMapper;
	}

	@Override
	public List<HouseCardVo> findHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto) {
		return houseMapper.findHousesBetweenCoordinates(requestDto);
	}

	@Override
	public List<HouseCardVo> findHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto) {
		return houseMapper.findHousesClosestToCoordinates(requestDto);
	}

	@Override
	public List<HouseDealVo> findAllHouseDealsByAptSeqWithCursor(String aptSeq, Long cursorId, int size) {
		return houseMapper.findAllHouseDealsByAptSeqWithCursor(aptSeq, cursorId, size);
	}

	@Override
	public HouseDetailVo getHouseDetailByAptSeq(String aptSeq) {
		return houseMapper.getHouseDetailByAptSeq(aptSeq);
	}

	@Override
	public HouseDealStatVo getHouseDealStatByAptSeq(String aptSeq) {
		HouseDealStatVo result = houseMapper.findHouseDealStatByAptSeq(aptSeq);
		if (result == null) {
			throw new ResourceNotFoundException("아파트 거래 통계 없음");
		}
		return result;
	}
}
