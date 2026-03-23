package com.aladdin.core_service.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "housedeals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseDeals {

    @Id
    @Column(name="no")
    private Long no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apt_seq")
    private HouseInfo houseInfo;

    @Column(name="apt_dong")
    private String aptDong;

    @Column(name="floor")
    private String floor;

    @Column(name="deal_year")
    private Integer dealYear;

    @Column(name="deal_month")
    private Integer dealMonth;

    @Column(name="deal_day")
    private Integer dealDay;

    @Column(name="exclu_use_ar")
    private Double excluUseAr;

    @Column(name="deal_amount")
    private String dealAmount;

    @Column(name="views", columnDefinition = "INT DEFAULT 0")
    private Integer views;

}
