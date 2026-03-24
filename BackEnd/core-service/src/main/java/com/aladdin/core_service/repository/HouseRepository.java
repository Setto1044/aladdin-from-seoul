package com.aladdin.core_service.repository;

import com.aladdin.core_service.dto.HouseMapClusterDto;
import com.aladdin.core_service.entity.HouseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<HouseInfo, String>, HouseCustomRepository {
}
