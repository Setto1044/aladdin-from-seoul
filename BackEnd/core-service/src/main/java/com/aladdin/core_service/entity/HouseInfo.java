package com.aladdin.core_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="houseinfos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseInfo {

    @Id
    @Column(name="apt_seq")
    private String aptSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_code")
    private DongCode dongCode;

    @Column(name="sgg_cd")
    private String sggCode;

    @Column(name="umd_cd")
    private String umdCode;

    @Column(name="umd_nm")
    private String umdName;

    @Column(name="jibun")
    private String jibun;

    @Column(name="road_nm_sgg_cd")
    private String roadNameSggCode;

    @Column(name="road_nm")
    private String roadName;

    @Column(name="road_nm_bonbun")
    private String roadNameBonbun;

    @Column(name="road_nm_bubun")
    private String roadNameBubun;

    @Column(name="apt_nm")
    private String aptName;

    @Column(name="build_year")
    private String buildYear;

    @Column(name="latitude")
    private String latitude;

    @Column(name="longitude")
    private String longitude;



}
