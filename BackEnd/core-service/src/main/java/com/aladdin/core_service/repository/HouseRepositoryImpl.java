package com.aladdin.core_service.repository;

import com.aladdin.core_service.dto.HouseSummaryNearbyRequestDto;
import com.aladdin.core_service.entity.HouseInfo;
import com.aladdin.core_service.entity.QHouseDealsStat;
import com.aladdin.core_service.entity.QHouseInfo;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
}
