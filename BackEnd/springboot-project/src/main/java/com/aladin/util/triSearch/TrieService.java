package com.aladin.util.triSearch;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrieService {
	// Trie 초기화
	public void initialize(List<String> addresses) {
		for (String address : addresses) {
			TrieSearch.insert(address); // 주소 삽입
		}
		log.debug("Tri 초기화 완료");
	}

	// 키워드 검색
	public List<String> search(String keyword) {
		return TrieSearch.search(keyword); // 주소 검색
	}
}