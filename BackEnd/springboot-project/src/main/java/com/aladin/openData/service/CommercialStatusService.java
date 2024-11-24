package com.aladin.openData.service;

import java.util.List;

import com.aladin.openData.vo.CommercialStatusVO;

public interface CommercialStatusService {
	CommercialStatusVO getCommercialStatusByGuCode(String dongCode);

	List<CommercialStatusVO> getAllCommercialStatuses();
}
