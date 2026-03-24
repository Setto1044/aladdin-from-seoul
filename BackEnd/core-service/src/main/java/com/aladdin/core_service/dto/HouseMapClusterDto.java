package com.aladdin.core_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HouseMapClusterDto {
    private String regionName;  // sido_name or gugun_name
    private Long count;
}
