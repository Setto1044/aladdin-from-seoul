package com.aladdin.core_service.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class HouseMapSearchResponseDto {

    private int total;
    private MapSearchScope scope;
    private List<HouseSummaryDto> houseSummaries;
    private List<HouseMapClusterDto> clusters;

    public static HouseMapSearchResponseDto ofHouse(List<HouseSummaryDto> houseSummaries) {
        return HouseMapSearchResponseDto.builder()
                .total(houseSummaries.size())
                .houseSummaries(houseSummaries)
                .build();
    }

    public static HouseMapSearchResponseDto ofClusters(List<HouseMapClusterDto> clusters, MapSearchScope scope) {
        return HouseMapSearchResponseDto.builder()
                .scope(scope)
                .total(clusters.stream().mapToInt(c -> c.getCount().intValue()).sum())
                .clusters(clusters)
                .build();
    }

}
