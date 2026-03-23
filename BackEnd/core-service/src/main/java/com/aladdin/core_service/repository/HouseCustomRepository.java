package com.aladdin.core_service.repository;

import com.aladdin.core_service.entity.HouseInfo;

import java.util.List;

public interface HouseCustomRepository {
    List<HouseInfo> getHouseSummaryNearby(Double latitude, Double longitude, Double distance);
    List<HouseInfo> getHouseSummaryInGrid(double minLat, double maxLat, double minLon, double maxLon);
}
