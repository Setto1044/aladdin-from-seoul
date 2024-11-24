package com.aladin.openData.mapper;

import java.util.List;

import com.aladin.openData.vo.CommercialStatusVO;

public interface CommercialStatusMapper {
	CommercialStatusVO findOneByGuCode(String guCode);

	List<CommercialStatusVO> findAll();
}
