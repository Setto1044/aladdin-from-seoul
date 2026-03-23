package com.aladdin.core_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "houseinfo_details")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseInfoDetails {

    @Id
    @Column(name = "apt_seq")
    private String aptSeq;

    @OneToOne
    @MapsId
    @JoinColumn(name="apt_seq")
    private HouseInfo houseInfo;

    @Column(columnDefinition = "TEXT")
    private String aptName;

    @Column(name = "household_count", columnDefinition = "TEXT")
    private String householdCount;

    @Column(name = "floor_range", columnDefinition = "TEXT")
    private String floorRange;

    @Column(name = "parking_count", columnDefinition = "TEXT")
    private String parkingCount;

    @Column(name = "floor_area_ratio")
    private Integer floorAreaRatio;

    @Column(name = "building_coverage_ratio")
    private Integer buildingCoverageRatio;

    @Column(name = "construction_company", columnDefinition = "TEXT")
    private String constructionCompany;

    @Column(name = "heating_type", columnDefinition = "TEXT")
    private String heatingType;

    @Column(name = "management_digit", columnDefinition = "TEXT")
    private String managementDigit;

    @Column(name = "area_sizes", columnDefinition = "TEXT")
    private String areaSizes;

}
