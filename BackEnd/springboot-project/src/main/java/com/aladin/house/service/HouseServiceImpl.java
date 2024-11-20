package com.aladin.house.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.mapper.HouseMapper;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.house.vo.HouseDealVo;

@Service
public class HouseServiceImpl implements HouseService {
	private final HouseMapper houseMapper;

	public HouseServiceImpl(HouseMapper houseMapper) {
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
	public List<HouseDealVo> findAllHouseDealsByAptSeq(String aptSeq) {
		return houseMapper.findAllHouseDealsByAptSeq(aptSeq);
	}

}
