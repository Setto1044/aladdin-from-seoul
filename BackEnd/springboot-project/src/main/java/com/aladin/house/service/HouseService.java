package com.aladin.house.service;

import java.util.List;
import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.vo.HouseCardVo;

public interface HouseService {
    // 특정 범위의 데이터 가져오기
    List<HouseCardVo> findHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto);

    // 특정 위도, 경도를 기준으로 가까운 데이터 가져오기
    List<HouseCardVo> findHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto);
}