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
		List<HouseCardVo> houseCardVos = triMapper.findAllAptSeqAndAptNameOfHouseCardsInSeoul();
		// Insert data into TrieSearch
		for (DongCodeVo apt : dongCodes) {
			TrieSearch.insert(apt.getDongCode(), apt.getSidoName() + " " + apt.getGugunName() + " " + apt.getDongName());
		}
		for (HouseCardVo houseCardVo : houseCardVos) {
			TrieSearch.insert(houseCardVo.getDongCode(), houseCardVo.getAptName());
		}
	}

	public List<String> searchKeywords(String keyword) {
		return TrieSearch.search(keyword);
	}
}