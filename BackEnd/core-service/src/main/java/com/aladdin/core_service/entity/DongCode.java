package com.aladdin.core_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="dongcodes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DongCode {
    @Id
    @Column(name = "dong_code", length = 10)
    private String dongCode;

    @Column(name="sido_name", length = 30)
    private String sidoName;

    @Column(name="gugun_name", length = 30)
    private String gugunName;

    @Column(name="dong_name", length = 30)
    private String dongName;

}
