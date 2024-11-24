package com.aladin.util.triSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Trie 노드 정의
class TrieNode {
	Map<Character, TrieNode> children = new HashMap<>();
	List<String> results = new ArrayList<>();
}

public class TrieSearch {
	private static final TrieNode root = new TrieNode(); // Trie 루트 노드

	// 주소 삽입 메서드
	public static void insert(String address) {
		for (int i = 0; i < address.length(); i++) {
			TrieNode node = root;
			for (int j = i; j < address.length(); j++) {
				char c = address.charAt(j);
				node.children.putIfAbsent(c, new TrieNode());
				node = node.children.get(c);
				// 중복된 주소가 없을 때만 추가
				if (!node.results.contains(address)) {
					node.results.add(address);
				}
			}
		}
	}

	// 키워드 검색 메서드
	public static List<String> search(String keyword) {
		TrieNode node = root;
		for (char c : keyword.toCharArray()) {
			if (!node.children.containsKey(c)) {
				return new ArrayList<>(); // 검색어와 일치하는 데이터 없음
			}
			node = node.children.get(c);
		}
		return new ArrayList<>(node.results); // 결과 반환
	}
}