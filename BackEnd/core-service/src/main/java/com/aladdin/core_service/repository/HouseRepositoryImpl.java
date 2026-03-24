package com.aladdin.core_service.repository;

import com.aladdin.core_service.dto.HouseMapClusterDto;
import com.aladdin.core_service.entity.HouseInfo;
import com.aladdin.core_service.entity.QDongCode;
import com.aladdin.core_service.entity.QHouseDealsStat;
import com.aladdin.core_service.entity.QHouseInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QHouseInfo houseInfo = QHouseInfo.houseInfo;
    private final QHouseDealsStat stat = QHouseDealsStat.houseDealsStat;

    @Override
    public List<HouseInfo> getHouseSummaryNearby(Double latitude, Double longitude, Double distance) {
        String pointWkt = String.format("POINT(%f %f)", latitude, longitude);

        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText({1}, 4326))",
                houseInfo.location, pointWkt);


        return queryFactory
                .selectFrom(houseInfo)
                .leftJoin(houseInfo.stat, stat).fetchJoin()
                .where(
                        houseInfo.location.isNotNull(),
                        distanceExpr.loe(distance)
                )
                .orderBy(distanceExpr.asc())
                .fetch();

    }

    @Override
    public List<HouseInfo> getHouseSummaryInGrid(double minLat, double maxLat, double minLon, double maxLon) {
        return queryFactory
                .selectFrom(houseInfo)
                .leftJoin(houseInfo.stat, stat).fetchJoin()
                .where(
                        houseInfo.latitude.between(minLat, maxLat),
                        houseInfo.longitude.between(minLon, maxLon)
                )
                .fetch();
    }

    @Override
    public List<HouseMapClusterDto> getClusterCount(Double latitude, Double longitude, double distanceMeter, boolean byDistrict) {
        double latDelta = distanceMeter / 111_000.0;
        double lonDelta = distanceMeter / (111_000.0 * Math.cos(Math.toRadians(latitude)));

        QDongCode dongCode = QDongCode.dongCode1;
        StringPath nameTarget = byDistrict ? dongCode.sidoName : dongCode.gugunName;

        return queryFactory
                .select(Projections.constructor(HouseMapClusterDto.class,
                        nameTarget,
                        houseInfo.count()
                ))
                .from(houseInfo)
                .join(houseInfo.dongCode, dongCode)
                .where(
                        houseInfo.latitude.between(latitude - latDelta, latitude + latDelta),
                        houseInfo.longitude.between(longitude - lonDelta, longitude + lonDelta),
                        dongCode.dongName.isNotNull()
                )
                .groupBy(nameTarget)
                .orderBy(houseInfo.count().desc())
                .fetch();
    }
}
