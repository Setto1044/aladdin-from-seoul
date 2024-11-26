package com.aladin.util.triSearch;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aladin.house.vo.DongCodeVo;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.util.triSearch.mapper.TrieMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrieService {
	private final TrieMapper triMapper;

	public TrieService(TrieMapper triMapper) {
		this.triMapper = triMapper;
	}

	@PostConstruct
	public void initializeTrieSearch() {
		// Fetch data from the database using AptMapper
		List<DongCodeVo> dongCodes = triMapper.findAllDongCodes();

		for (DongCodeVo dongcodeVo : dongCodes) {
			TrieSearch.insert(dongcodeVo.getDongCode(), dongcodeVo.getDongName() + " " + dongcodeVo.getSidoName() + " " + dongcodeVo.getGugunName());
		}

		List<HouseCardVo> housecards = triMapper.findAllAptSeqAndAptNameOfHouseCardsInSeoul();
		for (HouseCardVo housecard : housecards) {
			TrieSearch.insert(housecard.getAptSeq(), housecard.getAptName());
		}

	}

	public List<String> searchKeywords(String keyword) {
		return TrieSearch.search(keyword);
	}
}