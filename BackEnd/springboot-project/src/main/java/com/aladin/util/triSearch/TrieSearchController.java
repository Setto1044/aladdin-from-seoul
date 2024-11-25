package com.aladin.util.triSearch;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aladin.common.ApiResponseDto;

@RestController
@RequestMapping("/trie")
public class TrieSearchController {

	private final TrieService trieService;

	public TrieSearchController(TrieService trieService) {
		super();
		this.trieService = trieService;
	}

	// 추천 검색어 API
	@GetMapping("/search")
	public ResponseEntity<ApiResponseDto<List<String>>> searchDong(@RequestParam String keyword) {
		List<String> results = trieService.searchKeywords(keyword);
		return ResponseEntity.ok(ApiResponseDto.of(true, "추천 검색어 조회 성공", results));
	}
}
