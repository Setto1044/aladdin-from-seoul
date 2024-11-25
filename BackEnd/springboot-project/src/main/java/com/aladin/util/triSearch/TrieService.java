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
	public void initializeTrie() {
		List<DongCodeVo> dongCodes = triMapper.findAllDongCodes();
		List<String> addresses = dongCodes.stream().map(d -> (d.getDongCode()) + ", " + (d.getSidoName() != null ? d.getSidoName() : "") + " " + (d.getGugunName() != null ? d.getGugunName() : "") + " " + (d.getDongName() != null ? d.getDongName() : "")).map(String::trim) // 문자열의 앞뒤 공백 제거
				.toList();

// List<HouseCardVo> aptSeqAndNames = houseMapper.findAllAptSeqAndAptNameOfHouseCards();
		List<HouseCardVo> aptSeqAndNames = triMapper.findAllAptSeqAndAptNameOfHouseCardsInSeoul();
		List<String> seqNames = aptSeqAndNames.stream().map(a -> (a.getAptSeq()) + ", " + (a.getAptName())).map(String::trim).toList();

		initializeKeyWordsInList(addresses); // Trie에 주소 삽입
		initializeKeyWordsInList(seqNames); // Trie에 아파트 삽입

		log.debug("Tri 초기화 완료");
	}

	// Trie 초기화
	public static void initializeKeyWordsInList(List<String> keywords) {
		for (String keyword : keywords) {
			TrieSearch.insert(keyword); // 주소 삽입
		}
	}

	public List<String> searchKeywords(String keyword) {
		return TrieSearch.search(keyword);
	}

}