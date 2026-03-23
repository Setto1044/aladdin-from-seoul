package com.aladdin.core_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.geolatte.geom.Point;

@Entity
@Table(name="houseinfos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseInfo {

    @Id
    @Column(name="apt_seq", length = 20)
    private String aptSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dong_code")
    private DongCode dongCode;

    @Column(name="sgg_cd", length = 5)
    private String sggCode;

    @Column(name="umd_cd", length = 5)
    private String umdCode;

    @Column(name="umd_nm", length = 20)
    private String umdName;

    @Column(name="jibun", length = 10)
    private String jibun;

    @Column(name="road_nm_sgg_cd", length = 5)
    private String roadNameSggCode;

    @Column(name="road_nm", length = 20)
    private String roadName;

    @Column(name="road_nm_bonbun", length = 10)
    private String roadNameBonbun;

    @Column(name="road_nm_bubun", length = 10)
    private String roadNameBubun;

    @Column(name="apt_nm", length = 40)
    private String aptName;

    @Column(name="build_year")
    private Integer buildYear;

    @Column(name="latitude", length = 45)
    private String latitude;

    @Column(name="longitude", length = 45)
    private String longitude;

    @Column(name = "location", columnDefinition = "POINT")
    private Point location;

    @OneToOne(mappedBy = "houseInfo", fetch = FetchType.LAZY)
    private HouseDealsStat stat;

}
