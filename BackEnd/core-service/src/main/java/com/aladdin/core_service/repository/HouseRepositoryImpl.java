package com.aladdin.core_service.repository;

import com.aladdin.core_service.dto.HouseMapClusterDto;
import com.aladdin.core_service.entity.HouseInfo;
import com.aladdin.core_service.entity.QDongCode;
import com.aladdin.core_service.entity.QHouseDealsStat;
import com.aladdin.core_service.entity.QHouseInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QHouseInfo houseInfo = QHouseInfo.houseInfo;
    private final QHouseDealsStat stat = QHouseDealsStat.houseDealsStat;
    private final EntityManager em;

    @Override
    public List<HouseInfo> getHouseSummaryNearby(Double latitude, Double longitude, Double distance) {
        double latDelta = distance / 111_000.0;
        double lngDelta = distance / (111_000.0 * Math.cos(Math.toRadians(latitude)));

        // MySQL SRID 4326 = (위도 경도) 순서
        String centerWkt = String.format("POINT(%f %f)", latitude, longitude);
        String polygonWkt = String.format(
                "POLYGON((%f %f,%f %f,%f %f,%f %f,%f %f))",
                latitude - latDelta, longitude - lngDelta,
                latitude + latDelta, longitude - lngDelta,
                latitude + latDelta, longitude + lngDelta,
                latitude - latDelta, longitude + lngDelta,
                latitude - latDelta, longitude - lngDelta
        );

        String sql = String.format("""
                SELECT hi.*
                FROM houseinfos hi
                WHERE hi.location IS NOT NULL
                  AND MBRContains(ST_GeomFromText('%s', 4326), hi.location)
                  AND ST_Distance_Sphere(hi.location, ST_GeomFromText('%s', 4326)) <= %f
                ORDER BY ST_Distance_Sphere(hi.location, ST_GeomFromText('%s', 4326))
                """, polygonWkt, centerWkt, distance, centerWkt);

        return em.createNativeQuery(sql, HouseInfo.class).getResultList();
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
