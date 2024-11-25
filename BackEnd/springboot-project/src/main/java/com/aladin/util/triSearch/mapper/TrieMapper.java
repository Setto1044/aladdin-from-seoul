package com.aladin.util.triSearch.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.aladin.house.vo.DongCodeVo;
import com.aladin.house.vo.HouseCardVo;

@Mapper
public interface TrieMapper {

	List<DongCodeVo> findAllDongCodes();

	List<HouseCardVo> findAllAptSeqAndAptNameOfHouseCards();

	List<HouseCardVo> findAllAptSeqAndAptNameOfHouseCardsInSeoul();
}
