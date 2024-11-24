package com.aladin.house.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aladin.exceptions.ResourceNotFoundException;
import com.aladin.house.dto.BetweenRangeHouseCardRequestDto;
import com.aladin.house.dto.ClosestCoordinateHouseCardRequestDto;
import com.aladin.house.dto.HouseDealWithDongCodeDto;
import com.aladin.house.mapper.HouseMapper;
import com.aladin.house.vo.DongCodeVo;
import com.aladin.house.vo.HouseCardVo;
import com.aladin.house.vo.HouseDealStatVo;
import com.aladin.house.vo.HouseDetailVo;
import com.aladin.util.triSearch.TrieService;

import jakarta.annotation.PostConstruct;

@Service
public class HouseServiceImpl implements HouseService {
	private final HouseMapper houseMapper;
	private final TrieService trieService; // TrieService 의존성

	public HouseServiceImpl(HouseMapper houseMapper, TrieService trieService) {
		super();
		this.houseMapper = houseMapper;
		this.trieService = trieService;
	}

	/**
	 * Tri 사용 추천 (시 구 동) 키워드 데이터 제공
	 */
	@PostConstruct
	public void initializeTrie() {
		// mapper에서 `dongcodes` vo 데이터를 조회하고 하나의 문자열 주소로 변환해 Trie에 삽입
		List<DongCodeVo> dongCodes = houseMapper.findAllDongCodes();
		// 주소 데이터만 추출하여 Trie 초기화
		List<String> addresses = dongCodes.stream()
				.map(d -> (d.getSidoName() != null ? d.getSidoName() : "") + " " + (d.getGugunName() != null ? d.getGugunName() : "") + " "
						+ (d.getDongName() != null ? d.getDongName() : ""))
				.map(String::trim) // 문자열의 앞뒤 공백 제거
				.toList();
		trieService.initialize(addresses); // Trie에 주소만 삽입
	}

	@Override
	public List<String> searchDong(String keyword) {
		return trieService.search(keyword); // Trie에서 검색
	}

	@Override
	public List<HouseCardVo> findHousesBetweenCoordinates(BetweenRangeHouseCardRequestDto requestDto) {
		return houseMapper.findHousesBetweenCoordinates(requestDto);
	}

	@Override
	public List<HouseCardVo> findHousesClosestToCoordinates(ClosestCoordinateHouseCardRequestDto requestDto) {
		return houseMapper.findHousesClosestToCoordinates(requestDto);
	}

	@Override
	public List<HouseDealWithDongCodeDto> findAllHouseDealsByAptSeqWithCursor(String aptSeq, Long cursorId, int size) {
		return houseMapper.findAllHouseDealsByAptSeqWithCursor(aptSeq, cursorId, size);
	}

	@Override
	public HouseDetailVo getHouseDetailByAptSeq(String aptSeq) {
		return houseMapper.getHouseDetailByAptSeq(aptSeq);
	}

	@Override
	public HouseDealStatVo getHouseDealStatByAptSeq(String aptSeq) {
		HouseDealStatVo result = houseMapper.findHouseDealStatByAptSeq(aptSeq);
		if (result == null) {
			throw new ResourceNotFoundException("아파트 거래 통계 없음");
		}
		return result;
	}

}
