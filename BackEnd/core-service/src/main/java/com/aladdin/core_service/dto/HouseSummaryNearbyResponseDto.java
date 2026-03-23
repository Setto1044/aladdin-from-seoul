package com.aladdin.core_service.dto;

import com.aladdin.core_service.entity.HouseDealsStat;
import com.aladdin.core_service.entity.HouseInfo;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HouseSummaryNearbyResponseDto {

    private int total;
    private List<HouseSummaryDto> houseSummaries;

    public static HouseSummaryNearbyResponseDto of(List<HouseSummaryDto> houseSummaries) {
        return HouseSummaryNearbyResponseDto.builder()
                .total(houseSummaries.size())
                .houseSummaries(houseSummaries)
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    public static class HouseSummaryDto {
        private String aptSeq;
        private String aptName;
        private String latitude;
        private String longitude;

        private String sidoName;
        private String gugunName;
        private String dongName;

        private String minPrice;
        private String maxPrice;
        private Double minArea;
        private Double maxArea;

        public static HouseSummaryDto of(HouseInfo info) {
            HouseDealsStat stat = info.getStat();
            return HouseSummaryDto.builder()
                    .aptSeq(info.getAptSeq())
                    .aptName(info.getAptName())
                    .latitude(info.getLatitude())
                    .longitude(info.getLongitude())
                    .sidoName(info.getDongCode().getSidoName())
                    .gugunName(info.getDongCode().getGugunName())
                    .dongName(info.getDongCode().getDongName())
                    .minPrice(stat != null ? stat.getDealAmountMin() : "정보없음")
                    .maxPrice(stat != null ? stat.getDealAmountMax() : "정보없음")
                    .minArea(stat != null ? stat.getExcluUseArMin() : 0.0)
                    .maxArea(stat != null ? stat.getExcluUseArMax() : 0.0)
                    .build();
        }
    }



}
