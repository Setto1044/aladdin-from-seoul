package com.aladin.viewCount.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViewCountMapper {
	int incrementHouseDealViews(Long no);

	int incrementRoomBoardViews(Long id);
}
