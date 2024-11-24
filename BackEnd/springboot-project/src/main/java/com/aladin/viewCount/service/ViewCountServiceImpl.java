package com.aladin.viewCount.service;

import org.springframework.stereotype.Service;

import com.aladin.viewCount.mapper.ViewCountMapper;

@Service
public class ViewCountServiceImpl implements ViewCountService {
	private final ViewCountMapper viewCountMapper;

	public ViewCountServiceImpl(ViewCountMapper viewCountMapper) {
		this.viewCountMapper = viewCountMapper;
	}

	public void incrementHouseDealViews(Long houseDealNo) {
		int updatedRows = viewCountMapper.incrementHouseDealViews(houseDealNo);
		if (updatedRows == 0) {
			throw new RuntimeException("조회수 증가 실패: 해당 거래 내역을 찾을 수 없습니다.");
		}
	}

	@Override
	public void incrementRoomBoardViews(Long roomBoardId) {
		int updatedRows = viewCountMapper.incrementRoomBoardViews(roomBoardId);
		if (updatedRows == 0) {
			throw new RuntimeException("조회수 증가 실패: 해당 게시물을 찾을 수 없습니다.");
		}

	}
}
