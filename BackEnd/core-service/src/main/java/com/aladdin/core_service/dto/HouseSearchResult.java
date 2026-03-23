package com.aladdin.core_service.dto;

import com.aladdin.core_service.entity.HouseInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseSearchResult {
    private String aptSeq;
    private String aptName;
    private String umdName;
    private String roadName;

    public static HouseSearchResult of(HouseInfo info) {
        return HouseSearchResult.builder()
                .aptSeq(info.getAptSeq())
                .aptName(info.getAptName())
                .umdName(info.getUmdName())
                .roadName(info.getRoadName())
                .build();
    }
}
