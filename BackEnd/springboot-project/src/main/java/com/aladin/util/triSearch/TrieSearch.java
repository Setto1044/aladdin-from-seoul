package com.aladin.util.triSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Trie 자료구조의 노드를 정의
class TrieNode {
	Map<Character, TrieNode> children = new HashMap<>();
	List<String> results = new ArrayList<>();
}

public class TrieSearch {

	// Trie의 루트 노드
	static TrieNode root = new TrieNode();

	// 모든 위치에서 시작하는 부분 문자열을 Trie에 삽입하는 메서드
	public static void insert(String code, String address) {
		for (int i = 0; i < address.length(); i++) {
			TrieNode node = root;
			for (int j = i; j < address.length(); j++) {
				char c = address.charAt(j);
				node.children.putIfAbsent(c, new TrieNode());
				node = node.children.get(c);
				// 중복된 결과를 방지하기 위해 결과 리스트에 존재하지 않는 경우에만 추가
				if (!node.results.contains(code + ", " + address)) {
					node.results.add(code + ", " + address);
				}
			}
		}
		// System.out.println("Inserted: " + code + " " + address); // 디버깅용 출력
	}

	// 검색어로 Trie에서 데이터를 검색하는 메서드
	public static List<String> search(String keyword) {
		TrieNode node = root;
		for (char c : keyword.toCharArray()) {
			if (!node.children.containsKey(c)) {
				System.out.println("Character not found: " + c); // 디버깅용 출력
				return new ArrayList<>();
			}
			node = node.children.get(c);
		}
		// 해당 노드에서 시작하는 모든 결과 수집 (중복 제거)
		return new ArrayList<>(node.results);
	}
}
