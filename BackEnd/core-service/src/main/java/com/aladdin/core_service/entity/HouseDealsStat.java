package com.aladdin.core_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;

@Entity
@Table(name = "housedeals_stat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HouseDealsStat {

    @Id
    @Column(name = "apt_seq")
    private String aptSeq;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // HouseInfo PK(aptSeq)-> housedeals PK
    @JoinColumn(name = "apt_seq")
    private HouseInfo houseInfo;

    @Column(name = "deal_date_oldest")
    private Date dealDateOldest;

    @Column(name = "deal_date_latest")
    private Date dealDateLatest;

    @Column(name = "exclu_use_ar_min")
    private Double excluUseArMin;

    @Column(name = "exclu_use_ar_max")
    private Double excluUseArMax;

    @Column(name = "deal_amount_min", length = 10)
    private String dealAmountMin;

    @Column(name = "deal_amount_max", length = 10)
    private String dealAmountMax;

}
