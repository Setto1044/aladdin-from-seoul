package com.aladin.openData.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.openData.mapper.CommercialStatusMapper;
import com.aladin.openData.vo.CommercialStatusVO;

@Service
public class CommercialStatusServiceImpl implements CommercialStatusService {

	private final CommercialStatusMapper commercialStatusMapper;

	@Autowired
	public CommercialStatusServiceImpl(CommercialStatusMapper commercialStatusMapper) {
		this.commercialStatusMapper = commercialStatusMapper;
	}

	@Override
	public CommercialStatusVO getCommercialStatusByGuCode(String dongCode) {
		CommercialStatusVO result = commercialStatusMapper.findOneByGuCode(getGuCodeFromDongCode(dongCode));
		if (result == null) {
			throw new ResourceNotFoundException("동코드에 해당하는 서울시의 구가 존재하지 않습니다.");
		}
		return result;
	}

	@Override
	public List<CommercialStatusVO> getAllCommercialStatuses() {
		return commercialStatusMapper.findAll();
	}

	public static String getGuCodeFromDongCode(String dongCode) {
		if (dongCode.length() < 5) {
			throw new IllegalArgumentException("잘못된 동코드입니다.");
		}
		return dongCode.substring(0, 5);
	}
}
